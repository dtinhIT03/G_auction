package com.ghtk.auction.dto.request.vnpay;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VNPayPaymentResponse {
    private String code;
    private String message;
    private String paymentUrl;
}
