package kr.kro.gonggibap.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSearchResponse {
    private Long restaurantId;
    private String restaurantName;
    private String restaurantLink;
    private String restaurantCategory;
    private String restaurantAddress;
    private String restaurantRoadAddress;
    private BigDecimal restaurantLatitude;
    private BigDecimal restaurantLongitude;
}
