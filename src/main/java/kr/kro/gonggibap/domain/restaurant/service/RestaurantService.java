package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantPageResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantPageResponse getRestaurant(List<BigDecimal> latitudes, List<BigDecimal> longitudes, Pageable pageable) {

        StringBuilder polygon = new StringBuilder("POLYGON((");
        polygon.append(latitudes.get(0)).append(" ").append(longitudes.get(0)).append(", ")
                .append(latitudes.get(1)).append(" ").append(longitudes.get(1)).append(", ")
                .append(latitudes.get(2)).append(" ").append(longitudes.get(2)).append(", ")
                .append(latitudes.get(3)).append(" ").append(longitudes.get(3)).append(", ")
                .append(latitudes.get(0)).append(" ").append(longitudes.get(0)).append("))");

        log.info("지도 조회 POLYGON 검색 범위 : {}", polygon);

        Page<RestaurantResponse> restaurantResponses = restaurantRepository.getRestaurant(polygon.toString(), pageable);

        return new RestaurantPageResponse(restaurantResponses.getTotalPages(),
                restaurantResponses.getContent());
    }
}
