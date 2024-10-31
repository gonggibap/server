package kr.kro.gonggibap.domain.history.service;

import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.history.dto.response.HistoryResponse;
import kr.kro.gonggibap.domain.history.entity.History;
import kr.kro.gonggibap.domain.history.repository.HistoryRepository;
import kr.kro.gonggibap.domain.history.service.validator.HistoryValidator;
import kr.kro.gonggibap.domain.restaurant.repository.RestaurantRepository;
import kr.kro.gonggibap.domain.restaurant.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static kr.kro.gonggibap.core.error.ErrorCode.NOT_FOUND_RESTAURANT;
import static kr.kro.gonggibap.domain.history.service.validator.HistoryValidator.validateHistory;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final RestaurantRepository restaurantRepository;

    /**
     * 특정 식당 ID를 통해 해당 식당의 구청 방문 기록 반환
     * 우선 조회하려는 레스토랑 id가 있는지 유효성 검사
     *
     * @param restaurantId
     * @return List<HistoryResponse>
     */
    public PageResponse<HistoryResponse> getHistory(Long restaurantId, Pageable pageable) {

        // 우선 레스토링이 존재하는지 확인
        if(!restaurantRepository.existsById(restaurantId)) {
            throw new CustomException(NOT_FOUND_RESTAURANT);
        }

        Page<HistoryResponse> histories = historyRepository.findHistoryByRestaurantId(restaurantId, pageable);

        validateHistory(histories);

        return new PageResponse<>(histories.getTotalPages(),
                histories.getContent());
    }

}
