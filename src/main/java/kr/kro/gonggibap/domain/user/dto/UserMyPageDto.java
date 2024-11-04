package kr.kro.gonggibap.domain.user.dto;

import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.entity.FavoriteRestaurant;
import kr.kro.gonggibap.domain.review.dto.response.ReviewResponse;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserMyPageDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private List<RestaurantResponse> userFavoriteRestaurants;
    private List<ReviewResponse> userReviews;

    public static UserMyPageDto of(User user,
                                   List<RestaurantResponse> favoriteRestaurants,
                                   List<ReviewResponse> reviewResponse) {
        UserMyPageDto dto = new UserMyPageDto();
        dto.userId = user.getId();
        dto.userName = user.getName();
        dto.userEmail = user.getEmail();
        dto.userFavoriteRestaurants = favoriteRestaurants;
        dto.userReviews = reviewResponse;
        return dto;
    }
}
