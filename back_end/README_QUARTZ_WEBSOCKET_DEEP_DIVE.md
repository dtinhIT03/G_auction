# Quartz + WebSocket Deep Dive (Backend Auction)

Tài liệu này đi sâu vào **luồng xử lý thực tế trong code** của project cho 2 phần quan trọng:
1) Quartz Scheduler (động theo từng phiên đấu giá),
2) WebSocket/STOMP (realtime bid/comment/control).

---

## 1) Cần gì để dùng Quartz trong project này?

## 1.1 Dependencies
Trong `pom.xml`, backend đã bật đủ nhóm dependency cho lịch động:
- `spring-boot-starter-quartz`
- `spring-boot-starter-web` (runtime app)

## 1.2 Cấu hình Quartz
Trong `application.yaml`:
- `spring.quartz.job-store-type: jdbc` -> job/trigger lưu DB (không mất khi restart process).
- `spring.quartz.jdbc.initialize-schema: never` -> không tự tạo schema, cần chuẩn bị bảng Quartz trước.
- `threadPool.threadCount: 7` -> số worker chạy job đồng thời.
- `jobStore.driverDelegateClass: StdJDBCDelegate` -> delegate JDBC chuẩn.

## 1.3 Bean/Service cần có
- Spring tự cung cấp `Scheduler` bean khi bật starter Quartz.
- `JobSchedulerServiceImpl` inject `Scheduler` để tạo `JobDetail + Trigger` động theo mỗi auction.
- Các class job (`UpdateAuctionStatus`, `RedisOpenAuction`, `RedisActiveAuction`, `RedisEndAuction`) implement `org.quartz.Job` và gọi service nghiệp vụ.

---

## 2) Quartz trong dự án chạy như thế nào (luồng end-to-end)

## 2.1 Điểm bắt đầu: Admin confirm auction
Khi admin confirm, `AuctionServiceImpl.confirmAuction(...)`:
1. Tính 3 mốc runtime:
   - `endRegistration`
   - `startTime`
   - `endTime`
2. Update auction status thành `OPENING`.
3. Gọi 2 nhóm schedule:
   - `jobSchedulerService.scheduleStatusUpdates(auction)`
   - `jobSchedulerService.scheduleRedisAuction(auction)`

=> Đây là điểm biến 1 record auction thành nhiều scheduled tasks theo timestamp cụ thể.

## 2.2 Scheduler service tạo các job động
`JobSchedulerServiceImpl` tạo tổng cộng 6 lịch chính:

### A. Nhóm đổi trạng thái DB
- CLOSED tại `endRegistration`
- IN_PROGRESS tại `startTime`
- FINISHED tại `endTime`

Mỗi lịch tạo:
- `JobDetail` class `UpdateAuctionStatus`, có `JobDataMap` (`auctionId`, `auctionStatus`)
- `Trigger` one-time với `startAt(Date.from(localDateTime))`

### B. Nhóm điều khiển realtime room (Redis)
- `RedisOpenAuction` tại `endRegistration` -> mở phòng + set joinable
- `RedisActiveAuction` tại `startTime` -> set started=true + broadcast start
- `RedisEndAuction` tại `endTime` -> kết thúc room, chốt winner, flush DB

### C. Reliability options đã bật
Khi build job detail:
- `.storeDurably(true)`
- `.requestRecovery()`

=> Có chủ đích hỗ trợ recover tốt hơn khi worker/restart.

## 2.3 Job execute làm gì?
Mỗi job đọc `auctionId` trong `JobDataMap`, rồi gọi service tương ứng:
- `UpdateAuctionStatus.execute` -> `auctionService.updateStatus(request)`
- `RedisOpenAuction.execute` -> `auctionRealtimeService.openAuctionRoom(auctionId)`
- `RedisActiveAuction.execute` -> `auctionRealtimeService.startAuction(auctionId)`
- `RedisEndAuction.execute` -> `auctionRealtimeService.endAuction(auctionId)`

## 2.4 Sau khi end auction
`endAuction(...)` xử lý nghiệp vụ quan trọng:
1. Broadcast sự kiện end.
2. Đóng join session, xóa joinable.
3. Lấy `lastBid` từ Redis, chốt `buyerId`, `endBid` sang DB.
4. Persist lịch sử bid (`UserAuctionHistory`).
5. Dọn Redis keys: bids/comments/room.

---

## 3) Vì sao bài toán này hợp Quartz hơn `@Scheduled`?

`@Scheduled` tốt cho job lặp cố định (cron/fixedDelay). Nhưng hệ đấu giá cần:
- Mỗi auction có mốc riêng khi admin confirm (runtime dynamic),
- Mỗi auction cần nhiều one-shot trigger khác nhau,
- Cần lưu lịch xuống DB để survive restart.

Quartz đáp ứng tốt 3 nhu cầu trên.

---

## 4) Cần gì để dùng WebSocket/STOMP trong project này?

## 4.1 Dependencies
Trong `pom.xml` đã có:
- `spring-boot-starter-websocket`
- `spring-security-messaging`

## 4.2 Bật broker + endpoint
`StompConfig` làm 4 việc quan trọng:
1. `@EnableWebSocketMessageBroker`
2. Đăng ký endpoint handshake: `/ws`
3. Bật simple broker cho `/topic`, `/user`
4. Prefix app destination là `/app`

=> Client gửi vào `/app/**`, server push ra `/topic/**` hoặc `/user/**`.

## 4.3 Security cho STOMP
Bảo mật được đặt theo nhiều lớp:

### Lớp 1: Handshake
- `ProtocolJwtHandshakeInterceptor` đọc token từ header `Sec-WebSocket-Protocol` theo cặp `Authorization-Bearer, <token>`.

### Lớp 2: Inbound channel interceptors
`configureClientInboundChannel` đăng ký:
- `JwtInterceptor`: validate token cho CONNECT/SUBSCRIBE/SEND...
- `AuthInterceptor`: phân quyền theo mapping handler.
- `ConfirmSubscribeInterceptor`: xác nhận subscribe flow.

### Lớp 3: Destination authorization
`messageAuthorizationManager()` định nghĩa rule theo destination:
- Cho subscribe các kênh auction/user có auth.
- Chỉ role USER mới được send vào `/app/auction/**`.
- Còn lại deny-all.

---

## 5) WebSocket runtime flow trong project

## 5.1 Kết nối và subscribe
1. Client connect `ws://.../auction/ws`.
2. Handshake interceptor verify JWT và set session attributes.
3. Client subscribe các topic theo room:
   - `/topic/auction/{auctionId}/control`
   - `/topic/auction/{auctionId}/notifications`
   - `/topic/auction/{auctionId}/bids`
   - `/topic/auction/{auctionId}/comments`
4. `AuctionStompController` trả receipt ACK qua `/user/{id}/queue/receipts`.

## 5.2 User gửi bid/comment
- Client gửi tới:
  - `/app/auction/{id}/bid`
  - `/app/auction/{id}/comment`
- `AuctionStompController` gọi `auctionRealtimeService` xử lý.

## 5.3 Service + event + broadcast
Luồng bid chuẩn:
1. Validate room started + step price + last price.
2. Ghi `BidMessage` vào Redis (`list + last value`).
3. Publish `BidEvent`.
4. `AuctionEventStompListener` nhận event và broadcast tới `/topic/auction/{id}/bids`.

Luồng comment tương tự:
- Lưu comment DB -> publish `CommentEvent` -> broadcast `/topic/auction/{id}/comments`.

## 5.4 Control messages từ scheduler
Quartz jobs gọi nghiệp vụ room:
- open room -> event `AuctionRoomOpenEvent` -> notify joinable user qua `/user/{id}/queue/control`
- start room -> event `AuctionStartEvent` -> broadcast `start` vào `/topic/auction/{id}/control`
- end room -> event `AuctionEndEvent` -> broadcast `end` vào `/topic/auction/{id}/control`

=> Realtime control không cần polling HTTP.

---

## 6) Checklist triển khai thực tế (nếu dựng lại từ đầu)

## 6.1 Quartz checklist
- [ ] Có dependency `spring-boot-starter-quartz`
- [ ] Chuẩn bị schema Quartz tables trong DB
- [ ] Bật `job-store-type=jdbc`
- [ ] Thiết kế identity/job group tránh trùng
- [ ] Đưa dữ liệu tối thiểu vào `JobDataMap`
- [ ] Có cleanup/reconciliation nếu business cancel auction

## 6.2 WebSocket checklist
- [ ] Có dependency websocket + security-messaging
- [ ] `@EnableWebSocketMessageBroker`
- [ ] Tách rõ `/app` (inbound command) và `/topic|/user` (outbound)
- [ ] JWT handshake + interceptor inbound
- [ ] Destination-level authorization deny-by-default
- [ ] Có receipt/ack và rule khi reconnect

---

## 7) Trả lời phỏng vấn mẫu (ngắn gọn)

**Q: Vì sao dùng Quartz thay `@Scheduled`?**
- Vì mỗi auction sinh ra nhiều one-time task tại runtime theo timestamp khác nhau. Quartz hỗ trợ dynamic trigger, lưu lịch JDBC, recovery tốt khi service restart.

**Q: Vì sao dùng WebSocket thay HTTP polling?**
- Đấu giá cần realtime push (bid/start/end). WebSocket full-duplex, server chủ động push, giảm latency và giảm request dư thừa so với polling HTTP.

**Q: Redis để làm gì?**
- Lưu state tạm thời của room/bid/join để đọc ghi nhanh trong phiên. Kết thúc phiên mới chốt dữ liệu quan trọng về DB để đảm bảo consistency business.

---

## 8) Code map tham chiếu nhanh

- Quartz config: `src/main/resources/application.yaml`
- Quartz orchestration: `service/impl/JobSchedulerServiceImpl.java`
- Quartz jobs: `scheduler/jobs/*.java`
- Confirm + schedule: `service/impl/AuctionServiceImpl.java`
- Realtime room service: `service/impl/AuctionRealtimeServiceImpl.java`
- Redis session repo: `repository/Custom/impl/AuctionSessionRepositoryImpl.java`
- STOMP config/security: `config/stomp/StompConfig.java`, `config/stomp/*Interceptor*.java`
- STOMP controller: `controller/auction/AuctionStompController.java`
- Event broadcast: `event/AuctionEventStompListener.java`
- STOMP outbound sender: `service/impl/StompServiceImpl.java`

