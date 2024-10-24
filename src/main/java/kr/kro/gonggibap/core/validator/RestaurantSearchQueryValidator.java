package kr.kro.gonggibap.core.validator;

import kr.kro.gonggibap.core.exception.CustomException;

import static kr.kro.gonggibap.core.error.ErrorCode.QUERY_EMPTY_ERROR;

public class RestaurantSearchQueryValidator {
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
