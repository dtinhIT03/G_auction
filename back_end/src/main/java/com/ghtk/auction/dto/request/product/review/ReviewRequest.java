package com.ghtk.auction.dto.request.product.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {
    Long productId;
    String content;
    Float rating;
    LocalDateTime createdAt;
}
