package kr.kro.gonggibap.domain.restaurant.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.kro.gonggibap.core.annotation.LoginUser;
import kr.kro.gonggibap.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Tag(name = "식당 좋아요 관리", description = "식당 좋아요 관리 API")
public interface FavoriteRestaurantControllerSwagger {
    @Operation(
            summary = "식당 좋아요",
            description = "로그인 된 사용자가 특정 식당을 좋아요함",
            parameters = {
                    @Parameter(name = AUTHORIZATION, description = "access token", in = ParameterIn.HEADER, required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰이 유효하지 않을 시 예외 발생",
                    content = @Content(mediaType = "application/json")
            )
    })
    @PostMapping("/{restaurantId}")
    ResponseEntity<?> doFavorite(@Parameter(hidden = true) @LoginUser User user, @PathVariable Long restaurantId);

    @Operation(
            summary = "식당 좋아요 취소",
            description = "로그인 된 사용자가 특정 식당을 좋아요를 취소함",
            parameters = {
                    @Parameter(name = AUTHORIZATION, description = "access token", in = ParameterIn.HEADER, required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "좋아요 취소 성공",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰이 유효하지 않을 시 예외 발생",
                    content = @Content(mediaType = "application/json")
            )
    })
    @DeleteMapping("/{restaurantId}")
    ResponseEntity<?> unFavorite(@Parameter(hidden = true) @LoginUser User user, @PathVariable Long restaurantId);

    @Operation(
            summary = "식당 좋아요 리스트 조회",
            description = "로그인 된 사용자가 좋아요한 식당 목록을 조회함",
            parameters = {
                    @Parameter(name = AUTHORIZATION, description = "access token", in = ParameterIn.HEADER, required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "리스트 조회",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰이 유효하지 않을 시 예외 발생",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("")
    ResponseEntity<?> getFavoriteRestaurants(@Parameter(hidden = true) @LoginUser User user,
                                             @PageableDefault(size = 20) Pageable pageable);

    @Operation(
            summary = "식당 좋아요 상태 조회",
            description = "로그인 된 사용자가 특정 식당의 좋아요 상태를 조회함",
            parameters = {
                    @Parameter(name = AUTHORIZATION, description = "access token", in = ParameterIn.HEADER, required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "상태 조회",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "토큰이 유효하지 않을 시 예외 발생",
                    content = @Content(mediaType = "application/json")
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "존재하지 않는 식당 ID 조회 시 예외 발생",
                    content = @Content(mediaType = "application/json")
            )
    })
    @GetMapping("/{restaurantId}/check")
    ResponseEntity<?> isFavoriteRestaurant(@Parameter(hidden = true) @LoginUser User user,
                                           @PathVariable Long restaurantId);
}
