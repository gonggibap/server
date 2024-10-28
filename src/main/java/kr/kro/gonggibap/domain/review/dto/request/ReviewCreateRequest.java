package kr.kro.gonggibap.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreateRequest {

    @NotNull(message = "리뷰 내용을 입력해주세요.")
    @NotBlank(message = "리뷰 내용을 입력해주세요.")
    private String content;

    @NotNull(message = "리뷰 점수를 입력해주세요.")
    private Double point;

    private List<MultipartFile> images;
}
