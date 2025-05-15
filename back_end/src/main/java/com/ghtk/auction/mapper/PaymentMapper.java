package com.ghtk.auction.mapper;

import com.ghtk.auction.dto.request.PaymentRequest;
import com.ghtk.auction.dto.response.PaymentResponse;
import com.ghtk.auction.entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PaymentMapper extends BaseMapper<Payment, PaymentResponse, PaymentRequest> {
}
