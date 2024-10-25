package kr.kro.gonggibap.domain.review.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@NotBlank
@AllArgsConstructor
public class ReviewCreateResponse {
    private Long reviewId;
}
