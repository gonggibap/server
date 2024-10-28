package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchResponse;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import kr.kro.gonggibap.domain.restaurant.repository.AddressRepository;
import kr.kro.gonggibap.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

import static kr.kro.gonggibap.core.error.ErrorCode.*;
import static kr.kro.gonggibap.domain.restaurant.service.validator.RestaurantQueryParser.parseQuery;
import static kr.kro.gonggibap.domain.restaurant.service.validator.RestaurantValidator.validateCoordinate;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressRepository addressRepository;

    /**
     * 식당 ID 기반 식당 조회
     *
     * @param restaurantId
     * @return
     */
    public Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_RESTAURANT));
    }

    /**
     * 범위 내 식당 조회
     * 조회 시 방문수 내림차순 정렬
     *
     * @param latitudes
     * @param longitudes
     * @param pageable
     * @return RestaurantPageResponse response
     */
    public PageResponse<?> getRestaurant(List<BigDecimal> latitudes, List<BigDecimal> longitudes, Pageable pageable) {

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
        } catch (Exception e) {
            throw new CustomException(COORDINATE_OUT_OF_BOUND);
        }

        return new PageResponse<>(restaurantResponses.getTotalPages(),
                restaurantResponses.getContent());
    }

    /**
     * 1. 음식 기반 검색 (ex. 피자, 치킨, 칼국수 치킨)
     * 2. 지역구 기반 검색 (ex. 강남구, 종로)
     * 3. 음식 및 지역구 기반 검색 (ex. 강남구 피자, 치킨 종로구, 용산 고기)
     *
     * @param query
     * @param pageable
     */
    public PageResponse<?> searchRestaurant(String query, Pageable pageable) {
        List<String> parseResult = parseQuery(query); // 파싱된 결과 값
        String district = parseResult.get(0);
        String food = parseResult.get(1);

        Page<RestaurantSearchResponse> restaurantSearchResponses;
        // 만약 지역명이 없다면
        if (!StringUtils.hasText(district)) {
            restaurantSearchResponses = restaurantRepository.searchRestaurantByFood(food, pageable);
        } else if (!StringUtils.hasText(food)) { // 지역명만 있을 경우
            log.info("district = {}", district);
            restaurantSearchResponses = restaurantRepository.searchRestaurantByDistrict(district, pageable);
        } else {
            restaurantSearchResponses = restaurantRepository.searchRestaurantByFoodAndDistrict(food, district, pageable);
        }

        return new PageResponse<>(restaurantSearchResponses.getTotalPages(),
                restaurantSearchResponses.getContent());
    }

    /**
     * 주소 코드 기반 식당 조회
     * 조회 시 방문수 내림차순 정렬
     *
     * @param dongCode
     * @param pageable
     * @return
     */
    public PageResponse<?> getRestaurantByAddressCode(String dongCode, Pageable pageable) {
        Page<RestaurantSearchResponse> pageResult = restaurantRepository.findByAddressCode(dongCode, pageable);
        List<RestaurantSearchResponse> content = pageResult.getContent();
        // 주소 코드 기반 식당 리스트가 비어있다면
        if (content.isEmpty()) {
            throw new CustomException(DONG_NOT_FOUND_ERROR);
        }

        int totalPages = pageResult.getTotalPages(); // 전체 페이지 수

        return new PageResponse<>(totalPages, content);
    }
}
