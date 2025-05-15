package com.ghtk.auction.dto.stomp;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuctionNewEndTimeMessage {
    LocalDateTime newEndTime;

}
