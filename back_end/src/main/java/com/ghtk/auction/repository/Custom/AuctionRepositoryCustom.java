package com.ghtk.auction.repository.Custom;

import com.ghtk.auction.dto.response.auction.AuctionListResponse;
import com.ghtk.auction.enums.AuctionStatus;
import com.ghtk.auction.enums.ProductCategory;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AuctionRepositoryCustom {

    List<AuctionListResponse> getAllAuctionListResponse(Pageable pageable, AuctionStatus status);
    List<AuctionListResponse> searchAuctionAdvance(Pageable pageable, AuctionStatus status, ProductCategory category, String startTime, String endTime,String name);
}
