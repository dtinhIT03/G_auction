package com.ghtk.auction.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ghtk.auction.enums.PaymentMethod;
import com.ghtk.auction.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Accessors(chain = true)
public class PaymentRequest {
    Long auction_id;
    Long buyer_id;
    PaymentStatus status;
    PaymentMethod paymentMethod;
    Long depositAmount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime deadline;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime datePayment;

}
