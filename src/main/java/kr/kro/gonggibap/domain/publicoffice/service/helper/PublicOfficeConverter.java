package kr.kro.gonggibap.domain.publicoffice.service.helper;

import kr.kro.gonggibap.domain.publicoffice.dto.response.PublicOfficeResponse;
import kr.kro.gonggibap.domain.publicoffice.dto.response.PublicOfficeRestaurantResponse;
import kr.kro.gonggibap.domain.publicoffice.entity.PublicOffice;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PublicOfficeConverter {
    public static PublicOfficeRestaurantResponse getPublicOfficeResponse(PublicOffice publicOffice, List<RestaurantResponse> restaurantResponses) {
        PublicOfficeResponse publicOfficeResponse = new PublicOfficeResponse(publicOffice.getId(),
                publicOffice.getName(),
                publicOffice.getAddress(),
                publicOffice.getRoadAddress(),
                publicOffice.getLatitude(),
                publicOffice.getLongitude()
        );

        return new PublicOfficeRestaurantResponse(publicOfficeResponse, restaurantResponses);
    }


    public static List<PublicOfficeResponse> getPublicOfficeResponses(List<PublicOffice> publicOffices) {
        return publicOffices.stream()
                .map(publicOffice -> new PublicOfficeResponse(publicOffice.getId(),
                publicOffice.getName(),
                publicOffice.getAddress(),
                publicOffice.getRoadAddress(),
                publicOffice.getLatitude(),
                publicOffice.getLongitude()
        )).toList();
    }
}
