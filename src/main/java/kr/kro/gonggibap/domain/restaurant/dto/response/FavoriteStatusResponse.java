package kr.kro.gonggibap.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteStatusResponse {
    private boolean favorite;
    private Long restaurantId;
}
