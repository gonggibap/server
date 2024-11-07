package kr.kro.gonggibap.domain.restaurant.controller;

import kr.kro.gonggibap.core.config.jwt.TokenProvider;
import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.dto.BlogPostDto;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantWithImageResponse;
import kr.kro.gonggibap.domain.restaurant.service.FavoriteRestaurantService;
import kr.kro.gonggibap.domain.restaurant.service.RestaurantBlogService;
import kr.kro.gonggibap.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static kr.kro.gonggibap.core.error.CommonResponse.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController implements RestaurantControllerSwagger {

    private final RestaurantService restaurantService;
    private final TokenProvider tokenProvider;
    private final FavoriteRestaurantService favoriteRestaurantService;
    private final RestaurantBlogService restaurantBlogService;
    /**
     * 범위 내 식당 조회
     * 조회 시 방문수 내림차순 정렬
     *
     * @param latitudes
     * @param longitudes
     * @param pageable
     * @return RestaurantPageResponse response
     */
    @GetMapping()
    public ResponseEntity<?> getRestaurants(@RequestParam(required = false) List<BigDecimal> latitudes,
                                            @RequestParam(required = false) List<BigDecimal> longitudes,
                                            @RequestParam(required = false) String category,
                                            @RequestParam(required = false) String search,
                                            @RequestParam(required = false) boolean favorite,
                                            @RequestHeader(name = "Authorization", required = false) String token,
                                            @PageableDefault(page = 0, size = 30) Pageable pageable) {
        PageResponse<?> response = null;

        // 로그인 된 사용자이면서 좋아요 목록을 호출한 경우
        if (favorite) {
            if (token == null) {
                log.info("로그인하지 않은 사용자 입니다.");
                throw new CustomException(ErrorCode.NOT_AUTHORIZATION);
            }

            // 로그인 한 사람
            Long userId = tokenProvider.getUserId(tokenProvider.getAccessToken(token));
            response = favoriteRestaurantService.getFavoriteRestaurants(userId, category, pageable);
        } else {
            log.info("비회원인 사용자 입니다.");
            response = restaurantService.getRestaurants(latitudes, longitudes, category, search, pageable);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * 식당 ID 단일 조회
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurant(@PathVariable Long id) {
        RestaurantWithImageResponse response = restaurantService.getRestaurant(id);
        return ResponseEntity.ok(success(response));
    }
    
    /**
     * 식당 ID로 포털 내 블로그 글 조회
     *
     * @param restaurantId
     * @return
     */
    @GetMapping("/{restaurantId}/blog")
    public ResponseEntity<?> getRestaurantBlogPost(@PathVariable Long restaurantId) {
        // BlogPost 리스트를 가져옴
        List<BlogPostDto> response = restaurantBlogService.searchBlogPostWithAPI(restaurantId);

        // API 응답에 블로그 포스트 리스트를 포함하여 반환
        return ResponseEntity.ok(success(response));
    }
}
