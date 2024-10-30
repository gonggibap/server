package kr.kro.gonggibap.domain.history.service.validator;

import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.history.dto.response.HistoryResponse;
import org.springframework.data.domain.Page;

import java.util.List;

import static kr.kro.gonggibap.core.error.ErrorCode.NOT_FOUND_HISTORY;

public class HistoryValidator {
    /**
     * 히스토리가 비었는지 확인해주는 코드
     * @param histories : 쿼리 결과 히스토리
     */
    public static void validateHistory(Page<HistoryResponse> histories) {
        if (histories.isEmpty()) {
            throw new CustomException(NOT_FOUND_HISTORY);
        }
    }
}
