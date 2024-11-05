package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantWithImageResponse;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import kr.kro.gonggibap.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private final AddressService addressService;

    @Value("${cloud.aws.base-url}")
    private String baseUrl;

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

    public PageResponse<?> getRestaurants(List<BigDecimal> latitudes, List<BigDecimal> longitudes, String category, String search, Pageable pageable) {

        Page<RestaurantResponse> restaurantResponses = Page.empty();
        List<String> parseResult = new ArrayList<>();

        // 검색 로직
        if(latitudes == null && longitudes == null && search != null) {
            parseResult = parseQuery(search);
            String district = parseResult.get(0);
            String food = parseResult.get(1);

            log.info("District: {} / Food: {}", district, food);

            // 식당 이름 검색
            if (!StringUtils.hasText(district)) { 
                restaurantResponses = restaurantRepository.searchRestaurantByFood(food, pageable);
            // 지역 검색
            } else if (!StringUtils.hasText(food)) {
                restaurantResponses = restaurantRepository.searchRestaurantByDistrict(district, pageable);
            // 식당 + 지역 검색
            } else {
                restaurantResponses = restaurantRepository.searchRestaurantByFoodAndDistrict(food, district, pageable);
            }
        // 현재 위치 지도 조회
        } else if (latitudes != null && longitudes != null){
            validateCoordinate(latitudes, longitudes);

            StringBuilder polygon = new StringBuilder("POLYGON((");
            polygon.append(latitudes.get(0)).append(" ").append(longitudes.get(0)).append(", ")
                    .append(latitudes.get(1)).append(" ").append(longitudes.get(1)).append(", ")
                    .append(latitudes.get(2)).append(" ").append(longitudes.get(2)).append(", ")
                    .append(latitudes.get(3)).append(" ").append(longitudes.get(3)).append(", ")
                    .append(latitudes.get(0)).append(" ").append(longitudes.get(0)).append("))");

            try {
                restaurantResponses = restaurantRepository.getRestaurants(polygon.toString(), category, pageable);
            } catch (Exception e) {
                throw new CustomException(COORDINATE_OUT_OF_BOUND);
            }
        } else {
            throw new CustomException(PARAMETER_MISSING_ERROR);
        }

        return new PageResponse<>(restaurantResponses.getTotalPages(),
                restaurantResponses.getContent());
    }

    public RestaurantWithImageResponse getRestaurant(Long id) {
        return  restaurantRepository.getRestaurantById(id, baseUrl)
                .orElseThrow(() -> new CustomException(NOT_FOUND_RESTAURANT));


    }

    /**
     * restaurantId를 기반으로 해당 아이디를 가진 엔티티가 존재하는지 확인하는 메소드
     * @param restaurantId
     * @return
     */
    public boolean existsById(Long restaurantId) {
        return restaurantRepository.existsById(restaurantId);
    }
}
