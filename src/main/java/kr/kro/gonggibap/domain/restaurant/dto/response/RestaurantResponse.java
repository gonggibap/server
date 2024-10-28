package kr.kro.gonggibap.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantResponse {
    private Long restaurantId;
    private String restaurantName;
    private String phone;
    private String restaurantLink;
    private String restaurantCategory;
    private String restaurantDetailCategory;
    private String restaurantAddressName;
    private String restaurantRoadAddressName;
    private BigDecimal restaurantLatitude;
    private BigDecimal restaurantLongitude;
    private Long publicOfficeId;
    private Long visitCount;
    private Double pointAvg;
}
