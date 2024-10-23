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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kr.kro.gonggibap.core.error.ErrorCode.*;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class RestaurantService {

    // 사용자 쿼리로부터 지역구 발견하기 한 서울 지역구 리스트
    private static final List<String> DISTRICTS = Arrays.asList(
            "중구", "종로구", "용산구", "성동구", "광진구", "동대문구", "중랑구",
            "성북구", "강북구", "도봉구", "노원구", "은평구", "서대문구", "마포구",
            "양천구", "강서구", "구로구", "금천구", "영등포구", "동작구", "관악구",
            "서초구", "강남구", "송파구", "강동구"
    );

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

    /**
     * 사용자의 검색어 중에서 구와 음식을 분리
     * (ex. 강남 칼국수 => district = '강남구', food = '칼국수'
     * @param word : 사용자 검색어 split한 단어
     * @return word에 대해 찾은 지역구, nullable
     */
    private String findDistrict(String word){
        for (String dist : DISTRICTS) {
            if (dist.contains(word)) { // 단어가 지역 이름을 포함하고 있다면
                return dist; // 가장 먼저 발견된 지역 이름 저장
            }
        }
        return null;
    }
    /**
     * 사용자의 검색어 중에서 구와 음식을 분리
     * (ex. 강남 칼국수 => district = '강남구', food = '칼국수'
     * @param query : 사용자 검색어
     * @return [지역구, 음식], nullable
     */
    private List<String> parseQuery(String query) {
        String district = null;
        List<String> foodWords = new ArrayList<>();

        // 공백 기준으로 문자열을 나누기
        String[] words = Arrays.stream(query.trim().split("\\s+"))
                .filter(word -> !word.isEmpty()) // 빈 문자열 필터링
                .toArray(String[]::new); // 결과를 배열로 변환

        // 사용자 검색어 유효성 검증
        validateQuery(words);

        // 단어 하나하나 비교
        for (String word : words) {
            // 해당 단어가 지역구 단어인지 확인
            String tmpDistrict = findDistrict(word);

            // 첫 지역구만 등록하도록 함(ex. 종로구 양천구 이렇게 오면 => 종로구만)
            if(district == null && tmpDistrict != null) district = tmpDistrict;

            // 해당 단어가 지역구 이름이 아니리면
            if (tmpDistrict == null) foodWords.add(word); // 음식으로 추가
        }
        // 음식 이름 합치기
        String food = foodWords.isEmpty() ? null : String.join(" ", foodWords);

        // 결과 출력
        log.info("District: {}, food: {}", district, food);

        return new ArrayList<>(Arrays.asList(district, food));
    }

    /**
     * 쿼리에 공백문자 등 유효하지 않은 값이 들어왔을 경우 예외처리 해주는 메소드
     * @param words : 단어 리스트
     */
    private void validateQuery(String[] words) {
        if (words.length < 1) {
            throw new CustomException(QUERY_EMPTY_ERROR);
        }
    } // Ensure this brace is correctly placed

    private void validateCoordinate(List<BigDecimal> latitudes, List<BigDecimal> longitudes){
        if(latitudes.size() != 4){
            throw new CustomException(LATITUDE_COUNT_ERROR);
        }

        if(longitudes.size() != 4){
            throw new CustomException(LONGITUDE_COUNT_ERROR);
        }
    }
}
