package kr.kro.gonggibap.domain.restaurant.dto.response;

import kr.kro.gonggibap.domain.publicoffice.entity.PublicOffice;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
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
    private String publicOfficeName;
    private Long visitCount;
    private Double pointAvg;

    public static RestaurantResponse of(Restaurant restaurant, PublicOffice publicOffice, Long visitCount, Double pointAvg) {
        RestaurantResponse response = new RestaurantResponse();
        response.restaurantId = restaurant.getId();
        response.restaurantName = restaurant.getRestaurantName();
        response.phone = restaurant.getPhone();
        response.restaurantLink = restaurant.getLink();
        response.restaurantCategory = restaurant.getCategory();
        response.restaurantDetailCategory = restaurant.getDetailCategory();
        response.restaurantAddressName = restaurant.getAddressName();
        response.restaurantRoadAddressName = restaurant.getRoadAddressName();
        response.restaurantLatitude = restaurant.getLatitude();
        response.restaurantLongitude = restaurant.getLongitude();
        response.publicOfficeId = publicOffice.getId();
        response.publicOfficeName = publicOffice.getName();
        response.visitCount = visitCount;
        response.pointAvg = pointAvg;
        return response;
    }
}
