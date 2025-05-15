package com.ghtk.auction.controller.product.review;

import com.ghtk.auction.dto.request.product.review.ReviewRequest;
import com.ghtk.auction.dto.response.ApiResponse;
import com.ghtk.auction.dto.response.product.review.ReviewResponse;
import com.ghtk.auction.dto.response.user.PageResponse;
import com.ghtk.auction.service.ReviewService;
import com.ghtk.auction.utils.AppConstants;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReviewController {
    ReviewService reviewService;
    //gui danh gia
    @PostMapping("/")
    ApiResponse<String> sendReview(@RequestBody ReviewRequest request){
        return ApiResponse.ok(reviewService.sendReview(request));
    }
    //phan trang
    @GetMapping("/getAll/{product_id}")
    ApiResponse<PageResponse<ReviewResponse>> getAllByProductId(@PathVariable Long product_id,
                                                                @RequestParam(value = "page_no", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
                                                                @RequestParam(value = "page_size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize
                                                                ){
        return ApiResponse.success(reviewService.getAllByProductId(product_id,pageNo,pageSize));

    }
}
