package kr.kro.gonggibap.domain.review.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.kro.gonggibap.core.annotation.LoginUser;
import kr.kro.gonggibap.domain.review.dto.request.ReviewCreateRequest;
import kr.kro.gonggibap.domain.user.entity.User;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Tag(name = "식당 리뷰", description = "식당 리뷰 관련 API")
public interface ReviewControllerSwagger {

    @Operation(summary = "리뷰 전체 조회",
            description = "모든 사용자가 특정 식당에 대한 리뷰 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/restaurant/{id}")
    ResponseEntity<?> getReviews(@PathVariable Long id);

    @Operation(summary = "리뷰 작성",
            description = "로그인한 사용자가 식당 리뷰 작성",
            parameters = {
                    @Parameter(name = AUTHORIZATION, description = "access token", in = ParameterIn.HEADER, required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "작성 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "파일 업로드 실패", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "리뷰 내용은 필수", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "리뷰 점수는 필수", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "식당을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/restaurant/{id}") ResponseEntity<?> createReview(@Parameter(hidden = true) @LoginUser User user,
                                                        @PathVariable Long id,
                                                        @Valid @ModelAttribute ReviewCreateRequest request);

    @Operation(summary = "리뷰 삭제",
            description = "로그인한 사용자가 작성한 식당 리뷰 삭제",
            parameters = {
                    @Parameter(name = AUTHORIZATION, description = "access token", in = ParameterIn.HEADER, required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "S3 파일 삭제 실패", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "삭제 권한이 없습니다.", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "리뷰를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteReview(@Parameter(hidden = true) @LoginUser User user,
                                    @PathVariable Long id);
    }
