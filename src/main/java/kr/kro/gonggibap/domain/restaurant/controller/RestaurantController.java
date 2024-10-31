package kr.kro.gonggibap.domain.restaurant.controller;

import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
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
    public ResponseEntity<?> getRestaurants(@RequestParam List<BigDecimal> latitudes,
                                            @RequestParam List<BigDecimal> longitudes,
                                            @RequestParam(required = false) String category,
                                            @PageableDefault(page = 0, size = 30) Pageable pageable) {

        PageResponse<?> response = restaurantService.getRestaurants(latitudes, longitudes, category, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 식당 ID 단일 조회
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getRestaurant(@PathVariable Long id) {
        RestaurantResponse response = restaurantService.getRestaurant(id);
        return ResponseEntity.ok(success(response));
    }


    /**
     * 주소 코드 기반 식당 조회
     * 조회 시 방문수 내림차순 정렬
     *
     * @param dongCode
     * @param pageable
     * @return
     */
    @GetMapping("/by-dong")
    public ResponseEntity<?> getRestaurantByAddressCode(@RequestParam String dongCode,
                                                        @PageableDefault(page = 0, size = 30) Pageable pageable) {
        PageResponse<?> response = restaurantService.getRestaurantByAddressCode(dongCode, pageable);
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자의 검색어에 따른 음식점 검색
     * 일치 여부(score)에 따른 내림차순 정렬
     *
     * @param query
     * @param pageable
     * @return RestaurantPageResponse response
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchRestaurant(@RequestParam String query,
                                              @PageableDefault(page = 0, size = 10) Pageable pageable) {
        PageResponse<?> response = restaurantService.searchRestaurant(query, pageable);
        return ResponseEntity.ok(response);
    }

}
