package com.ghtk.auction.service;

import com.ghtk.auction.dto.request.product.review.ReviewRequest;
import com.ghtk.auction.dto.response.product.review.ReviewResponse;
import com.ghtk.auction.dto.response.user.PageResponse;

public interface ReviewService {
    String sendReview(ReviewRequest request);
    PageResponse<ReviewResponse> getAllByProductId(Long productId, int pageNo, int pageSize);
}
