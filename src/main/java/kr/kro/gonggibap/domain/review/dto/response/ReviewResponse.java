package kr.kro.gonggibap.domain.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import kr.kro.gonggibap.domain.image.entity.Image;
import kr.kro.gonggibap.domain.review.entity.Review;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    private Long reviewId;
    private Long userId;
    private String userName;
    private Long userReviewCount;
    private Double userReviewAvg;
    private Double point;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime date;
    private List<String> imageUrls;

    public static ReviewResponse of(User user, Review review) {
        ReviewResponse response = new ReviewResponse();
        response.reviewId = review.getId();
        response.userId = user.getId();
        response.userName = user.getName();
        response.userReviewCount = user.getReviewsCount();
        response.userReviewAvg = user.getReviewsAverage();
        response.point = review.getPoint();
        response.content = review.getContent();
        response.date = review.getLastModifiedDate();
        response.imageUrls = review.getImages().stream()
                .map(Image::getImageUrl).toList();
        return response;
    }
}
