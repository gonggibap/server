package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantPageResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchPageResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchResponse;
import kr.kro.gonggibap.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static kr.kro.gonggibap.core.error.ErrorCode.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RestaurantService {


    private final RestaurantRepository restaurantRepository;

    public RestaurantPageResponse getRestaurant(List<BigDecimal> latitudes, List<BigDecimal> longitudes, Pageable pageable) {

        validateCoordinate(latitudes, longitudes);

        StringBuilder polygon = new StringBuilder("POLYGON((");
        polygon.append(latitudes.get(0)).append(" ").append(longitudes.get(0)).append(", ")
                .append(latitudes.get(1)).append(" ").append(longitudes.get(1)).append(", ")
                .append(latitudes.get(2)).append(" ").append(longitudes.get(2)).append(", ")
                .append(latitudes.get(3)).append(" ").append(longitudes.get(3)).append(", ")
                .append(latitudes.get(0)).append(" ").append(longitudes.get(0)).append("))");

        Page<RestaurantResponse> restaurantResponses;

        try {
            restaurantResponses = restaurantRepository.getRestaurant(polygon.toString(), pageable);
        } catch (Exception e){
            throw new CustomException(COORDINATE_OUT_OF_BOUND);
        }

        return new RestaurantPageResponse(restaurantResponses.getTotalPages(),
                restaurantResponses.getContent());
    }

    /**
     * 우선 간단한 쿼리로 날림
     * 쿼리 기반 full-text search를 박을 건지 (ex. 치킨, 칼국수)
     * 지역명 활용해서 검색하는 것은 별도의 로직 및 필터링 역할 생각해봐야 함
     */
    public RestaurantSearchPageResponse searchRestaurant(String query, Pageable pageable) {
        
        Page<RestaurantSearchResponse> restaurantSearchResponses = restaurantRepository.searchRestaurant(query, pageable);

        return new RestaurantSearchPageResponse(restaurantSearchResponses.getTotalPages(),
                restaurantSearchResponses.getContent());
    }

    private void validateCoordinate(List<BigDecimal> latitudes, List<BigDecimal> longitudes){
        if(latitudes.size() != 4){
            throw new CustomException(LATITUDE_COUNT_ERROR);
        }

        if(longitudes.size() != 4){
            throw new CustomException(LONGITUDE_COUNT_ERROR);
        }
    }
}
