package kr.kro.gonggibap.domain.restaurant.dto.response;

import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantWithImageResponse {
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
    private String restaurantImage;
    private Long publicOfficeId;
    private String publicOfficeName;
    private Long visitCount;
    private Double pointAvg;
}
