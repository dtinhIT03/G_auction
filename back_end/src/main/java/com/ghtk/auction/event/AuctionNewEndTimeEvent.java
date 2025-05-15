package com.ghtk.auction.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AuctionNewEndTimeEvent {
    Long auctionId;
    LocalDateTime newEndTime;
}
