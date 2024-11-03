package kr.kro.gonggibap.domain.restaurant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.kro.gonggibap.domain.restaurant.service.RestaurantUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.kro.gonggibap.core.error.CommonResponse.success;

@Tag(name = "공유 URL", description = "공유 URL 관리 API")
public interface RestaurantShareControllerSwagger {
    @Operation(
            summary = "단축 URL 생성", description = "단축할 URL을 보내주면 단축된 URL을 반환함")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "반환 성공",
                    content = @Content(mediaType = "application/json")
            ),
    })
    @PostMapping("/create")
    ResponseEntity<?> createShareLink(@RequestParam String originalUrl);

    @Operation(
            summary = "단축 URL 이동", description = "단축된 URL을 보내주면 실제 URL을 반환함")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "실제 URL 조회 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "없는 단축 URL 요청시 에러 발생",
                    content = @Content(mediaType = "application/json")
            ),
    })
    @GetMapping("/{shortUrl}")
    ResponseEntity<?> getOriginalUrl(@PathVariable String shortUrl);

}
