package kr.kro.gonggibap.domain.restaurant.controller;

import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantWithImageResponse;
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
public class RestaurantController implements RestaurantControllerSwagger{

    private final RestaurantService restaurantService;

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
                                            @PageableDefault(page = 0, size = 30) Pageable pageable) {

        PageResponse<?> response = restaurantService.getRestaurants(latitudes, longitudes, category, search, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 식당 ID 단일 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurant(@PathVariable Long id) {
        RestaurantWithImageResponse response = restaurantService.getRestaurant(id);
        return ResponseEntity.ok(success(response));
    }

}
