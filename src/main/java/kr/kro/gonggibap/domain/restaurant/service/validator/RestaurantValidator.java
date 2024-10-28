package kr.kro.gonggibap.domain.restaurant.service.validator;

import kr.kro.gonggibap.core.exception.CustomException;

import java.math.BigDecimal;
import java.util.List;

import static kr.kro.gonggibap.core.error.ErrorCode.*;

public class RestaurantValidator {
    public static void validateCoordinate(List<BigDecimal> latitudes, List<BigDecimal> longitudes){
        if(latitudes.size() != 4){
            throw new CustomException(LATITUDE_COUNT_ERROR);
        }

        if(longitudes.size() != 4){
            throw new CustomException(LONGITUDE_COUNT_ERROR);
        }
    }

    /**
     * 쿼리에 공백문자 등 유효하지 않은 값이 들어왔을 경우 예외처리 해주는 메소드
     * @param words : 단어 리스트
     */
    public static void validateQuery(String[] words) {
        if (words.length < 1) {
            throw new CustomException(QUERY_EMPTY_ERROR);
        }
    }
}
