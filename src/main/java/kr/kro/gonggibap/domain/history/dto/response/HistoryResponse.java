package kr.kro.gonggibap.domain.history.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponse {
    private Long restaurantId;
    private String restaurantName;
    private String publicOfficeName;
    private LocalDateTime historyDate;
    private Long peopleCount;
    private Long price;
    private String useContent;
    private String consumer;
}