package kr.kro.gonggibap.domain.restaurant.controller;

import kr.kro.gonggibap.core.annotation.LoginUser;
import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.service.FavoriteRestaurantService;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.kro.gonggibap.core.error.CommonResponse.success;

@RestController
@RequestMapping("/api/restaurants/favorite")
@RequiredArgsConstructor
public class FavoriteRestaurantController implements FavoriteRestaurantControllerSwagger{
    private final FavoriteRestaurantService favoriteRestaurantService;

    @PostMapping("/{restaurantId}")
    public ResponseEntity<?> doFavorite(@LoginUser User user,
                                        @PathVariable Long restaurantId) {
        favoriteRestaurantService.doFavoriteRestaurant(user, restaurantId);
        return ResponseEntity.ok()
                .build();
    }

    @DeleteMapping("/{restaurantId}")
    public ResponseEntity<?> unFavorite(@LoginUser User user,
                                        @PathVariable Long restaurantId) {
        favoriteRestaurantService.unFavoriteRestaurant(user, restaurantId);
        return ResponseEntity.ok()
                .build();
    }

    @GetMapping("")
    public ResponseEntity<?> getFavoriteRestaurants(@LoginUser User user,
                                                    @PageableDefault(size = 20) Pageable pageable) {
        PageResponse<?> response = favoriteRestaurantService.getFavoriteList(user, pageable);
        return ResponseEntity.ok(success(response));
    }
}
