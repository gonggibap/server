package kr.kro.gonggibap.domain.restaurant.service.validator;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static kr.kro.gonggibap.core.constant.Districts.DISTRICTS;
import static kr.kro.gonggibap.domain.restaurant.service.validator.RestaurantValidator.validateQuery;

@Slf4j
public class RestaurantQueryParser {
    /**
     * 사용자의 검색어 중에서 구와 음식을 분리
     * (ex. 강남 칼국수 => district = '강남구', food = '칼국수'
     *
     * @param query : 사용자 검색어
     * @return [지역구, 음식], nullable
     */
    public static List<String> parseQuery(String query) {
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
            if (district == null && tmpDistrict != null) district = tmpDistrict;

            // 해당 단어가 지역구 이름이 아니리면
            if (tmpDistrict == null) foodWords.add(word); // 음식으로 추가
        }
        // 음식 이름 합치기
        String food = foodWords.isEmpty() ? null : String.join(" ", foodWords);

        // 결과 출력
        log.info("District: {}, food: {}", district, food);

        return Arrays.asList(district, food);
    }

    /**
     * 들어온 단어가 지역구인지 찾아주는 메소드
     *
     * @param word : 사용자 검색어 split한 단어
     * @return word에 대해 찾은 지역구, nullable
     */
    private static String findDistrict(String word) {
        for (String dist : DISTRICTS) {
            if (dist.contains(word)) { // 단어가 지역 이름을 포함하고 있다면
                return dist; // 가장 먼저 발견된 지역 이름 저장
            }
        }
        return null;
    }
}
