package kr.kro.gonggibap.domain.review.controller;

import jakarta.validation.Valid;
import kr.kro.gonggibap.core.annotation.LoginUser;
import kr.kro.gonggibap.domain.review.dto.request.ReviewCreateRequest;
import kr.kro.gonggibap.domain.review.dto.request.ReviewUpdateRequest;
import kr.kro.gonggibap.domain.review.dto.response.ReviewCreateResponse;
import kr.kro.gonggibap.domain.review.dto.response.ReviewResponse;
import kr.kro.gonggibap.domain.review.dto.response.ReviewUpdateResponse;
import kr.kro.gonggibap.domain.review.service.ReviewService;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.kro.gonggibap.core.error.CommonResponse.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController implements ReviewControllerSwagger{

    private final ReviewService reviewService;

    /**
     * 특정 식당 리뷰 전체 조회
     * @param id
     * @return
     */
    @GetMapping("/restaurant/{id}")
    public ResponseEntity<?> getReviews(@PathVariable Long id) {
        List<ReviewResponse> reviews = reviewService.getReviews(id);

        return ResponseEntity.ok(success(reviews));
    }

    /**
     * 리뷰 작성
     * @param user
     * @param request 식당 ID, 리뷰 내용, 리뷰 점수, 이미지
     * @return
     */
    @PostMapping(value = "/restaurant/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReview(@LoginUser User user,
                                            @PathVariable Long id,
                                            @Valid @ModelAttribute ReviewCreateRequest request) {
        Long reviewId = reviewService.createReview(request, id, user);

        return ResponseEntity.ok(success(new ReviewCreateResponse(reviewId)));
    }

    /**
     * 리뷰 수정
     * @param user
     * @param request 리뷰 ID, 리뷰 내용, 리뷰 점수, 이미지
     * @return
     */
    @PatchMapping(value = "/{reviewId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateReview(@LoginUser User user,
                                          @PathVariable Long reviewId,
                                          @Valid @ModelAttribute ReviewUpdateRequest request) {

        Long updateReviewId = reviewService.updateReview(request, reviewId, user);

        return ResponseEntity.ok(success(new ReviewUpdateResponse(updateReviewId)));
    }


    /**
     * 리뷰 삭제
     * @param user
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@LoginUser User user,
                                            @PathVariable Long id) {
        reviewService.deleteReview(user, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyReviews(@LoginUser User user, Pageable pageable) {
        List<ReviewResponse> myReviews = reviewService.getMyPagingReviews(user, pageable);

        return ResponseEntity.ok(success(myReviews));
    }
}
