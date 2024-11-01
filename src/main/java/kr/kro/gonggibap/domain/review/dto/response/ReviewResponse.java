package kr.kro.gonggibap.domain.review.dto.response;

import kr.kro.gonggibap.domain.image.entity.Image;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class ReviewResponse {
    private final Long reviewId;
    private final Long userId;
    private final String userName;
    private final Long userReviewCount;
    private final Double userReviewAvg;
    private final Double point;
    private final String content;
    private final LocalDate date;
    private final List<String> imageUrls;
}
