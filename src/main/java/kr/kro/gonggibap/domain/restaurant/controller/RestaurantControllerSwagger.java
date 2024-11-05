package kr.kro.gonggibap.domain.restaurant.controller;

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
import org.springframework.web.bind.annotation.RequestHeader;
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
    ResponseEntity<?> getRestaurants(@RequestParam(required = false) List<BigDecimal> latitudes,
                                            @RequestParam(required = false) List<BigDecimal> longitudes,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) String search,
                                            @RequestParam(required = false) boolean favorite,
                                            @RequestHeader(required = false) String token,
                                            @PageableDefault(page = 0, size = 30) Pageable pageable);

    @Operation(
            summary = "식당 단일 조회", description = "식당 ID 대한 정보를 조회함")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "주어진 ID와 일치하는 식당이 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{id}")
    ResponseEntity<?> getRestaurant(@PathVariable Long id);

    @Operation(
            summary = "식당 관련 블로그 포스트 조회", description = "식당 ID로 블로그 포스트 조회")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "주어진 ID와 일치하는 식당이 없음",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("{restaurantId}")
    ResponseEntity<?> getRestaurantBlogPost(@PathVariable Long restaurantId);

}
