package kr.kro.gonggibap.domain.history.controller;

import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.domain.history.dto.response.HistoryResponse;
import kr.kro.gonggibap.domain.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.kro.gonggibap.core.error.CommonResponse.success;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/histories")
public class HistoryController implements HistoryControllerSwagger {

    private final HistoryService historyService;

    /**
     * 레스토랑 ID 기반 공공기관 히스토리 조회
     * 한 페이지당 10개씩 조회, 날짜 기준 최신순 정렬
     *
     * @param restaurantId
     * @param pageable
     * @return
     */
    @GetMapping("/{restaurantId}")
    public ResponseEntity<?> getHistory(@PathVariable Long restaurantId,
                                        @PageableDefault(page = 0, size = 5) Pageable pageable) {
        PageResponse<HistoryResponse> response = historyService.getHistory(restaurantId, pageable);


        return ResponseEntity.ok(success(response));
    }
}
