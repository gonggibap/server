package kr.kro.gonggibap.domain.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HistoryCountDto {
    private Long restaurantId;
    private Long historyCount;
}
