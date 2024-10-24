package kr.kro.gonggibap.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantSearchPageResponse {
    private Integer totalPages;
    private List<RestaurantSearchResponse> restaurantSearchResponse;
}
