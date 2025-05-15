package com.ghtk.auction.service.impl;

import com.ghtk.auction.dto.request.product.review.ReviewRequest;
import com.ghtk.auction.dto.response.auction.AuctionListResponse;
import com.ghtk.auction.dto.response.product.review.ReviewResponse;
import com.ghtk.auction.dto.response.user.PageResponse;
import com.ghtk.auction.dto.response.user.UserResponse;
import com.ghtk.auction.entity.Review;
import com.ghtk.auction.entity.User;
import com.ghtk.auction.repository.ReviewRepository;
import com.ghtk.auction.repository.UserRepository;
import com.ghtk.auction.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    UserRepository userRepository;
    @Override
    public String sendReview(ReviewRequest request) {
        var context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        User user = userRepository.findByEmail(email);

        Review review = Review.builder()
                .content(request.getContent())
                .rating(request.getRating())
                .createdAt(LocalDateTime.now())
                .productId(request.getProductId())
                .userId(user.getId())
                .build();
        Review review1 =  reviewRepository.save(review);
        return "đánh giá thành công";
    }

    @Override
    public PageResponse<ReviewResponse> getAllByProductId(Long productId,int pageNo, int pageSize) {
        int totalElement = reviewRepository.countAllByProductId(productId);
        List<Object[]> objects =  reviewRepository.getAllByProductId(productId,pageNo*pageSize,pageSize);
        List<Review> reviews = objects.stream().map(
                object -> Review.builder()
                        .id((Long) object[0])
                        .userId((Long) object[1])
                        .productId((Long) object[2])
                        .content((String) object[3])
                        .rating((Float) object[4])
                        .createdAt(((Timestamp) object[5]).toLocalDateTime())
                        .build()
        ).collect(Collectors.toList());
        List<Long> userIds = reviews.stream().map(review -> review.getUserId()).collect(Collectors.toList());
        List<User> users = userRepository.findAllById(userIds);
        // Map userId -> UserResponse
        Map<Long, UserResponse> userResponseMap = users.stream()
                .collect(Collectors.toMap(
                        User::getId,
                        user -> UserResponse.builder()
                                .avatar(user.getAvatar())
                                .fullName(user.getFullName())
                                .gender(user.getGender())
                                .build()
                ));

        // Bây giờ map thành List<ReviewResponse>
        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(review -> ReviewResponse.builder()
                        .content(review.getContent())
                        .rating(review.getRating())
                        .createdAt(review.getCreatedAt())
                        .userResponse(userResponseMap.get(review.getUserId()))
                        .build()
                )
                .collect(Collectors.toList());

        PageResponse<ReviewResponse> reviewResponsePageResponse = new PageResponse<>();
        reviewResponsePageResponse.setPageNo(pageNo);
        reviewResponsePageResponse.setPageSize(pageSize);
        reviewResponsePageResponse.setTotalElements(totalElement);
        reviewResponsePageResponse.setContent(reviewResponses);
        return reviewResponsePageResponse;
    }
}
