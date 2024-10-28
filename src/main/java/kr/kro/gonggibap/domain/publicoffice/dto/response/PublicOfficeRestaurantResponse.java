package kr.kro.gonggibap.domain.publicoffice.dto.response;


import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class PublicOfficeRestaurantResponse {
    private final PublicOfficeResponse publicOffice;
    private final List<RestaurantResponse> restaurants;
}
