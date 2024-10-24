package kr.kro.gonggibap.domain.review.controller;

import jakarta.validation.Valid;
import kr.kro.gonggibap.core.annotation.LoginUser;
import kr.kro.gonggibap.domain.review.dto.request.ReviewCreateRequest;
import kr.kro.gonggibap.domain.review.dto.response.ReviewCreateResponse;
import kr.kro.gonggibap.domain.review.dto.response.ReviewResponse;
import kr.kro.gonggibap.domain.review.service.ReviewService;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.kro.gonggibap.core.error.CommonResponse.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 특정 식당 리뷰 전체 조회
     * @param restaurantId
     * @return
     */
    @GetMapping()
    public ResponseEntity<?> getReviews(@RequestParam Long restaurantId){
        List<ReviewResponse> reviews = reviewService.getReviews(restaurantId);

        return ResponseEntity.ok(success(reviews));
    }

    /**
     * 리뷰 작성
     * @param user
     * @param request 식당 ID, 리뷰 내용, 리뷰 점수, 이미지
     * @return
     */
    @PostMapping()
    public ResponseEntity<?> createReview(@LoginUser User user,
                                            @Valid @ModelAttribute ReviewCreateRequest request){
        Long reviewId = reviewService.createReview(request, user);

        return ResponseEntity.ok(success(new ReviewCreateResponse(reviewId)));
    }

    /**
     * 리뷰 삭제
     * @param user
     * @param reviewId
     * @return
     */
    @DeleteMapping()
    public ResponseEntity<?> deleteReview(@LoginUser User user,
                                            @RequestParam Long reviewId){
        reviewService.deleteReview(user, reviewId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }
}
