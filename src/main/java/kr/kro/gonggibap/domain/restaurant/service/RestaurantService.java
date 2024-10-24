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
import static kr.kro.gonggibap.core.util.RestaurantSearchUtil.parseQuery;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    /**
     * 범위 내 식당 조회
     * 조회 시 방문수 내림차순 정렬
     * @param latitudes
     * @param longitudes
     * @param pageable
     * @return RestaurantPageResponse response
     */
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
     * 1. 음식 기반 검색 (ex. 피자, 치킨, 칼국수 치킨)
     * 2. 지역구 기반 검색 (ex. 강남구, 종로)
     * 3. 음식 및 지역구 기반 검색 (ex. 강남구 피자, 치킨 종로구, 용산 고기)
     * @param query
     * @param pageable
     */
    public RestaurantSearchPageResponse searchRestaurant(String query, Pageable pageable) {
        List<String> parseResult = parseQuery(query); // 파싱된 결과 값
        String district = parseResult.get(0);
        String food = parseResult.get(1);


        Page<RestaurantSearchResponse> restaurantSearchResponses;
        // 만약 지역명이 없다면
        if(district == null) {
            restaurantSearchResponses = restaurantRepository.searchRestaurantByFood(food, pageable);
        }
        // 지역명만 있을 경우
        else if (food == null){
            log.info(district);
            restaurantSearchResponses = restaurantRepository.searchRestaurantByDistrict(district, pageable);
        }
        else {
            restaurantSearchResponses = restaurantRepository.searchRestaurantByFoodAndDistrict(food, district, pageable);
        }

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

    /**
     * 주소 코드 기반 식당 조회
     * 조회 시 방문수 내림차순 정렬
     * @param dongCode
     * @param pageable
     * @return
     */
    public RestaurantPageResponse getRestaurantByAddressCode(String dongCode, Pageable pageable) {
        //restaurantRepository.findByAddressCode(dongCode, pageable);
        return null;
    }
}
