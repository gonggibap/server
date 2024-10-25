package kr.kro.gonggibap.domain.publicoffice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.kro.gonggibap.domain.publicoffice.dto.response.PublicOfficeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import static kr.kro.gonggibap.core.error.CommonResponse.success;

@Tag(name = "공공기관 (구청)", description = "공공기관 관련 API")
public interface PublicOfficeControllerSwagger {

    @Operation(summary = "공공기간 단일 조회",
            description = "모든 사용자가 특정 공공기관에 대한 리뷰"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "공공기관을 찾을 수 없습니다.", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    ResponseEntity<?> getPublicOffice(@PathVariable Long id);

    @Operation(summary = "공공기간 전체 조회",
            description = "모든 사용자가 전체 공공기관에 대한 리뷰"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("")
    ResponseEntity<?> getPublicOffices();
}
