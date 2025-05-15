package com.ghtk.auction.dto.response.product.review;

import com.ghtk.auction.dto.response.user.UserResponse;
import com.ghtk.auction.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    String content;
    Float rating;
    LocalDateTime createdAt;
    UserResponse userResponse;

}
