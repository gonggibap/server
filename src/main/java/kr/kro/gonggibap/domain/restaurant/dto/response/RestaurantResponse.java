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
    private String publicOfficeName;
    private Long visitCount;
    private Double pointAvg;

    public RestaurantResponse(final Long restaurantId, final String restaurantName, final String phone, final String restaurantLink, final String restaurantCategory, final String restaurantDetailCategory, final String restaurantAddressName, final String restaurantRoadAddressName, final BigDecimal restaurantLatitude, final BigDecimal restaurantLongitude, final Long publicOfficeId, final String publicOfficeName) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.phone = phone;
        this.restaurantLink = restaurantLink;
        this.restaurantCategory = restaurantCategory;
        this.restaurantDetailCategory = restaurantDetailCategory;
        this.restaurantAddressName = restaurantAddressName;
        this.restaurantRoadAddressName = restaurantRoadAddressName;
        this.restaurantLatitude = restaurantLatitude;
        this.restaurantLongitude = restaurantLongitude;
        this.publicOfficeId = publicOfficeId;
        this.publicOfficeName = publicOfficeName;
    }

    public void setVisitCount(final Long visitCount) {
        this.visitCount = visitCount;
    }
}
