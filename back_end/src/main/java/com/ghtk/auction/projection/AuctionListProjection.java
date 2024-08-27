package com.ghtk.auction.projection;

import com.ghtk.auction.enums.AuctionStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public interface AuctionListProjection {
    Long getId();
    Long getProductId();
    String getTitle();
    String getDescription();
    String getImage();
    LocalDateTime getCreatedAt();
    LocalDateTime getConfirmDate();
    LocalDateTime getEndRegistration();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    Long getStartBid();
    Long getPricePerStep();
    Long getEndBid();
    AuctionStatus getStatus();
}
