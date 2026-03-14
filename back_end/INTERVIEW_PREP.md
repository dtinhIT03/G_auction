# Backend Interview Prep: WebSocket, Quartz, Redis (Auction)

## 1) WebSocket/STOMP trong dự án

### Mục tiêu
- Cập nhật realtime theo phòng đấu giá: bid, comment, thông báo start/end.
- Tách 2 chiều:
  - Client gửi lệnh vào `/app/**` (`@MessageMapping`).
  - Server push ra `/topic/**` (broadcast) hoặc `/user/**` (private queue).

### Luồng chính
1. Client connect endpoint WebSocket `/ws`.
2. Handshake interceptor đọc token từ `Sec-WebSocket-Protocol`.
3. Inbound channel interceptor kiểm tra JWT cho CONNECT/SUBSCRIBE/SEND.
4. Client gửi bid/comment vào `/app/auction/{id}/**`.
5. Service nghiệp vụ ghi Redis/DB và publish event nội bộ.
6. Listener nhận event và broadcast qua STOMP topic tương ứng.

### Điểm cần nói khi phỏng vấn
- Dùng WebSocket để giảm polling, độ trễ thấp cho trạng thái phiên.
- Dùng STOMP để chuẩn hóa pub/sub destination, receipt, subscribe flow.
- Có chặn quyền bằng `AuthorizationManager<Message<?>>` cho từng destination.

## 2) Quartz Scheduler trong dự án

### Mục tiêu
- Mỗi phiên có nhiều mốc thời gian khác nhau:
  - đổi trạng thái OPENING -> CLOSED -> IN_PROGRESS -> FINISHED.
  - mở room redis, bắt đầu auction realtime, kết thúc auction realtime.

### Vì sao dùng Quartz thay `@Scheduled`
- `@Scheduled` phù hợp job lặp cố định theo cron/fixedDelay.
- Bài toán này cần **dynamic one-time jobs** theo từng auction (mỗi auction một timestamp khác nhau).
- Quartz hỗ trợ:
  - tạo Job/Trigger động theo runtime;
  - persistence JDBC (`spring.quartz.job-store-type=jdbc`);
  - recovery (`requestRecovery`) khi restart.

### Luồng chính
1. Admin confirm auction.
2. Service tính `endRegistration/startTime/endTime`.
3. `JobSchedulerService` tạo 6 job-trigger động cho status + redis realtime.
4. Đến giờ, từng Quartz Job gọi nghiệp vụ tương ứng.

## 3) Redis cache/session trong dự án đấu giá

### Dữ liệu để Redis
- Auction room state: owner, startBid, step, started.
- Joinable user set / đang join set.
- Last bid + list bids cho room.

### Vì sao dùng Redis
- Realtime bidding cần đọc/ghi cực nhanh, tránh ghi DB liên tục mỗi lần bid.
- Dữ liệu room mang tính tạm thời (session-like), phù hợp key-value/in-memory.
- Có cấu trúc Set/List/Value rất hợp để model joiners, bids, last bid.
- Khi end auction mới flush kết quả quan trọng về DB (winner, end bid, lịch sử).

### Điểm trade-off có thể nói
- Redis là cache/session, không phải source-of-truth cuối cùng cho báo cáo dài hạn.
- Cần cơ chế dọn key khi phiên kết thúc để tránh memory leak.
- Nếu scale nhiều instance thì cần cùng 1 Redis backend (đã đáp ứng bởi thiết kế hiện tại).

## 4) So sánh nhanh để trả lời phỏng vấn

### WebSocket vs HTTP
- HTTP: request/response, thường stateless, realtime thường phải polling.
- WebSocket: kết nối persistent, full-duplex, server push chủ động.
- Với đấu giá realtime, WebSocket giảm độ trễ và giảm số request dư thừa.

### WebSocket vs Kafka
- Kafka: message broker cho stream/event giữa service, throughput cao, durable log.
- WebSocket: kênh realtime tới client cuối (browser/mobile).
- Hai thứ bổ sung nhau: backend có thể dùng Kafka nội bộ, rồi gateway push WebSocket ra client.

## 5) Code map nhanh (để ôn)

- Cấu hình STOMP/WebSocket: `config/stomp/StompConfig.java`
- JWT handshake/channel cho STOMP: `config/stomp/*Interceptor*.java`
- STOMP controller nhận message: `controller/auction/AuctionStompController.java`
- Push message ra topic/user: `service/impl/StompServiceImpl.java`
- Event -> broadcast realtime: `event/AuctionEventStompListener.java`
- Scheduler orchestration: `service/impl/JobSchedulerServiceImpl.java`
- Quartz jobs: `scheduler/jobs/*.java`
- Confirm auction + schedule jobs: `service/impl/AuctionServiceImpl.java`
- Redis session repository: `repository/Custom/impl/AuctionSessionRepositoryImpl.java`
- Quartz/Redis config: `application.yaml`

