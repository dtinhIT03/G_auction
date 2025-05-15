package com.ghtk.auction.enums;

public enum PaymentStatus {
    PENDING,     // Chưa thanh toán
    PAID,        // Đã thanh toán
    FULLY_PAID,   // Admin đã chuyển tiền cho người bán
    REFUNDED     // Đã hoàn tiền cho người mua
}
