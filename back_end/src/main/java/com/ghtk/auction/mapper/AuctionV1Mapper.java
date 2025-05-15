package com.ghtk.auction.mapper;

import com.ghtk.auction.dto.request.auction.AuctionCreationRequest;
import com.ghtk.auction.dto.response.auction.AuctionV1Response;
import com.ghtk.auction.entity.Auction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class AuctionV1Mapper extends BaseMapper<Auction, AuctionV1Response, AuctionCreationRequest> {
}
