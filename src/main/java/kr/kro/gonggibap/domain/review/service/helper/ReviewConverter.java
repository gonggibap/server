package kr.kro.gonggibap.domain.review.service.helper;

import kr.kro.gonggibap.domain.image.entity.Image;
import kr.kro.gonggibap.domain.review.dto.response.ReviewResponse;
import kr.kro.gonggibap.domain.review.entity.Review;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ReviewConverter {
    public static List<ReviewResponse> getMyReviewResponses(User user, List<Review> myReviews) {
        return myReviews.stream()
                .map(myReview -> new ReviewResponse(myReview.getId(),
                        user.getId(),
                        user.getName(),
                        user.getReviewsCount(),
                        user.getReviewsAverage(),
                        myReview.getPoint(),
                        myReview.getContent(),
                        myReview.getLastModifiedDate(),
                        myReview.getImages().stream()
                                .map(Image::getImageUrl).toList()
                )).toList();
    }

    public static List<ReviewResponse> getReviewResponses(List<Review> reviews) {
        return reviews.stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getUser().getId(),
                        review.getUser().getName(),
                        review.getUser().getReviewsCount(),  // 리뷰 개수
                        review.getUser().getReviewsAverage(),  // 리뷰 평균
                        review.getPoint(),
                        review.getContent(),
                        review.getLastModifiedDate(),
                        review.getImages().stream()
                                .map(Image::getImageUrl).toList()
                )).toList();
    }
}
