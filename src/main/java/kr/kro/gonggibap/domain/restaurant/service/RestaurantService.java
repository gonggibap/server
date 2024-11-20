package kr.kro.gonggibap.domain.restaurant.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.history.repository.HistoryRepository;
import kr.kro.gonggibap.domain.restaurant.dto.HistoryCountDto;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static kr.kro.gonggibap.core.error.ErrorCode.COORDINATE_OUT_OF_BOUND;
import static kr.kro.gonggibap.core.error.ErrorCode.NOT_FOUND_RESTAURANT;
import static kr.kro.gonggibap.domain.restaurant.service.validator.RestaurantQueryParser.parseQuery;
import static kr.kro.gonggibap.domain.restaurant.service.validator.RestaurantValidator.validateCoordinate;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final AddressService addressService;
    private final ObjectMapper objectMapper;
    private final HistoryRepository historyRepository;

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

    public PageResponse<?> getRestaurants(List<BigDecimal> latitudes, List<BigDecimal> longitudes, String category, String search, Pageable pageable) throws JsonProcessingException {
        Page<RestaurantResponse> restaurantResponses = Page.empty();
        List<String> parseResult = new ArrayList<>();

        // 검색 로직
        if (latitudes == null && longitudes == null && search != null && category == null) {
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
        } else if (latitudes == null && longitudes == null && search == null) {
            if (category == null) {
                restaurantResponses = restaurantRepository.getRestaurantAll(pageable);
            } else {
                restaurantResponses = restaurantRepository.getRestaurantsWithCategory(category, pageable);
            }
        } else if (latitudes != null && longitudes != null) {
            validateCoordinate(latitudes, longitudes);

            StringBuilder polygon = new StringBuilder("POLYGON((");
            polygon.append(latitudes.get(0)).append(" ").append(longitudes.get(0)).append(", ")
                    .append(latitudes.get(1)).append(" ").append(longitudes.get(1)).append(", ")
                    .append(latitudes.get(2)).append(" ").append(longitudes.get(2)).append(", ")
                    .append(latitudes.get(3)).append(" ").append(longitudes.get(3)).append(", ")
                    .append(latitudes.get(0)).append(" ").append(longitudes.get(0)).append("))");
            try {
                log.info("Polygon: {}", polygon);
                // 1. 기본 데이터 가져오기 (히스토리 카운트 제외)
                List<RestaurantResponse> restaurantsWithMBR = getRestaurantsWithMBR(polygon.toString(), category);
                long totalPages = restaurantRepository.countRestaurants(polygon.toString(), category);

                // 2. 레스토랑 ID 목록 생성
                List<Long> restaurantIds = restaurantsWithMBR.stream()
                        .map(RestaurantResponse::getRestaurantId)
                        .toList();

                // 3. 히스토리 카운트 가져오기
                List<HistoryCountDto> historyCounts = historyRepository.findHistoryCounts(restaurantIds);

                // 4. 히스토리 카운트를 Map으로 변환
                Map<Long, Long> historyCountMap = historyCounts.stream()
                        .collect(Collectors.toMap(HistoryCountDto::getRestaurantId, HistoryCountDto::getHistoryCount));

                // 5. RestaurantResponse에 히스토리 카운트 추가
                restaurantsWithMBR.forEach(restaurant ->
                        restaurant.setVisitCount(historyCountMap.getOrDefault(restaurant.getRestaurantId(), 0L))
                );

                // 6. 정렬 (필요 시)
                List<RestaurantResponse> sortedRestaurants = restaurantsWithMBR.stream()
                        .sorted(Comparator.comparingLong(RestaurantResponse::getVisitCount).reversed())
                        .collect(Collectors.toList());

                // 7. 페이징 처리
                return new PageResponse<>((int) Math.ceil((double) totalPages / pageable.getPageSize()),
                        sortedRestaurants);
            } catch (Exception e) {
                throw new CustomException(COORDINATE_OUT_OF_BOUND);
            }
        }
        return new PageResponse<>(restaurantResponses.getTotalPages(),
                restaurantResponses.getContent());
    }

    public RestaurantWithImageResponse getRestaurant(Long id) {
        return restaurantRepository.getRestaurantById(id, baseUrl)
                .orElseThrow(() -> new CustomException(NOT_FOUND_RESTAURANT));


    }

    /**
     * restaurantId를 기반으로 해당 아이디를 가진 엔티티가 존재하는지 확인하는 메소드
     *
     * @param restaurantId
     * @return
     */
    public boolean existsById(Long restaurantId) {
        return restaurantRepository.existsById(restaurantId);
    }

    public List<RestaurantResponse> getRestaurantsWithMBR(String polygon, String category) {
        List<Object[]> results = restaurantRepository.searchWithMBR(polygon, category);

        return results.stream()
                .map(row -> new RestaurantResponse(
                        ((Long) row[0]), // r.id
                        (String) row[1],                  // r.restaurant_name
                        (String) row[2],                  // r.phone
                        (String) row[3],                  // r.link
                        (String) row[4],                  // r.category
                        (String) row[5],                  // r.detail_category
                        (String) row[6],                  // r.address_name
                        (String) row[7],                  // r.road_address_name
                        ((BigDecimal) row[8]), // r.latitude
                        ((BigDecimal) row[9]), // r.longitude
                        ((Long) row[10]),  // h.public_office_id
                        (String) row[11]                   // p.name
                ))
                .collect(Collectors.toList());
    }

}
