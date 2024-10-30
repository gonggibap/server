package kr.kro.gonggibap.domain.history.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "히스토리", description = "공공기관 사용 내역 히스토리 관련 API")
public interface HistoryControllerSwagger {

    @Operation(summary = "히스토리 전체 조회",
            description = "특정 레스토랑 ID에 대해 히스토리 전체 조회"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "식당 또는 히스토리를 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{restaurantId}")
    ResponseEntity<?> getHistory(@PathVariable Long restaurantId,
                                 @PageableDefault(page = 0, size = 10) Pageable pageable);
}
