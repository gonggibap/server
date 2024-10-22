package kr.kro.gonggibap.domain.restaurant.controller;

import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantPageResponse;
import kr.kro.gonggibap.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping()
    public ResponseEntity<?> getRestaurant(@RequestParam List<BigDecimal> latitudes,
                                                        @RequestParam List<BigDecimal> longitudes,
                                                        @PageableDefault(page = 0, size = 30) Pageable pageable){

        RestaurantPageResponse response = restaurantService.getRestaurant(latitudes, longitudes, pageable);
        return ResponseEntity.ok(response);
    }
}
