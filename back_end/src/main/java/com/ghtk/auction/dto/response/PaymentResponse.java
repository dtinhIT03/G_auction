package com.ghtk.auction.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ghtk.auction.dto.response.auction.AuctionV1Response;
import com.ghtk.auction.dto.response.user.UserResponse;
import com.ghtk.auction.enums.PaymentMethod;
import com.ghtk.auction.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Accessors(chain = true)
@Builder
public class PaymentResponse {

    Long id;

    AuctionV1Response auctionRes;

    UserResponse buyerRes;

    PaymentStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt;
    Long depositAmount;

    PaymentMethod paymentMethod;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime deadline;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime datePayment;
}
