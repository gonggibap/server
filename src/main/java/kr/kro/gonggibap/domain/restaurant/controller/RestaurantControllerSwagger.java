package kr.kro.gonggibap.domain.restaurant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "식당 관리", description = "식당 관리 API")
public interface RestaurantControllerSwagger {
    @Operation(
            summary = "식당 조회", description = "좌표값을 토대로 식당에 대한 정보를 조회함. 기본 30개씩 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "파라미터 누락 / 4개의 경도, 위도가 아닐 시 예외 발생",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping()
    ResponseEntity<?> getRestaurant(@RequestParam List<BigDecimal> latitudes,
                                    @RequestParam List<BigDecimal> longitudes,
                                    @RequestParam String category,
                                    @PageableDefault(page = 0, size = 30) Pageable pageable);

    @Operation(
            summary = "동 기반 식당 조회", description = "동 코드를 토대로 식당에 대한 정보를 조회함. 기본 30개씩 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "동 코드가 존재하지 않을 시 예외 발생",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/by-dong")
    ResponseEntity<?> getRestaurantByAddressCode(@RequestParam String dongCode,
                                                 @PageableDefault(page = 0, size = 30) Pageable pageable);

    @Operation(
            summary = "동 기반 식당 조회", description = "동 코드를 토대로 식당에 대한 정보를 조회함. 기본 30개씩 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "동 코드가 존재하지 않을 시 예외 발생",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/search")
    ResponseEntity<?> searchRestaurant(@RequestParam String query,
                                       @PageableDefault(page = 0, size = 30) Pageable pageable);
}
