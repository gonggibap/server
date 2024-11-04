package kr.kro.gonggibap.domain.restaurant.controller;

import kr.kro.gonggibap.core.annotation.LoginUser;
import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.FavoriteStatusResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.service.FavoriteRestaurantService;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.kro.gonggibap.core.error.CommonResponse.success;

@RestController
@RequestMapping("/api/restaurants/favorite")
@RequiredArgsConstructor
public class FavoriteRestaurantController implements FavoriteRestaurantControllerSwagger {
    private final FavoriteRestaurantService favoriteRestaurantService;

    /**
     * 특정 식당에 사용자가 좋아요를 추가하는 API
     * JWT 토큰이 있어야한다.
     *
     * @param user
     * @param restaurantId
     * @return
     */
    @PostMapping("/{restaurantId}")
    public ResponseEntity<?> doFavorite(@LoginUser User user,
                                        @PathVariable Long restaurantId) {
        favoriteRestaurantService.doFavoriteRestaurant(user, restaurantId);
        return ResponseEntity.ok()
                .build();
    }

    /**
     * 특정 식당에 사용자가 남긴 좋아요를 삭제하는 API
     * JWT 토큰이 있어야한다.
     * @param user
     * @param restaurantId
     * @return
     */
    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<?> unFavorite(@LoginUser User user,
                                        @PathVariable Long restaurantId) {
        favoriteRestaurantService.unFavoriteRestaurant(user, restaurantId);
        return ResponseEntity.ok()
                .build();
    }

    /**
     * 사용자가 좋아요를 남긴 식당 리스트를 조회하는 API
     * JWT 토큰이 있어야 한다.
     * 페이징 처리를 지원한다.
     *
     * @param user
     * @param pageable
     * @return
     */
    @GetMapping("")
    public ResponseEntity<?> getFavoriteRestaurants(@LoginUser User user,
                                                    @PageableDefault(size = 20) Pageable pageable) {
        Page<RestaurantResponse> content = favoriteRestaurantService.getFavoritePagingList(user, pageable);
        PageResponse<RestaurantResponse> response = new PageResponse<>(content.getTotalPages(), content.getContent());
        return ResponseEntity.ok(success(response));
    }

    /**
     * 특정 레스토랑 ID에 대해 사용자의 좋아요 상태를 확인하는 API
     * 만약 레스토랑 ID가 존재하지 않는다면 404 Not Found 예외 발생
     * @param user
     * @param restaurantId
     * @return
     */
    @GetMapping("/{restaurantId}/check")
    public ResponseEntity<?> isFavoriteRestaurant(@LoginUser User user,
                                                  @PathVariable Long restaurantId) {
        boolean isFavoriteRestaurant = favoriteRestaurantService.isFavoriteRestaurant(user, restaurantId);
        return ResponseEntity.ok(success(new FavoriteStatusResponse(isFavoriteRestaurant, restaurantId)));
    }
}
