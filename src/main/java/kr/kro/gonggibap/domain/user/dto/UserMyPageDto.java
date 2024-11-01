package kr.kro.gonggibap.domain.user.dto;

import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.entity.FavoriteRestaurant;
import kr.kro.gonggibap.domain.review.dto.response.ReviewResponse;
import kr.kro.gonggibap.domain.user.entity.User;

import java.util.List;

public class UserMyPageDto {
    private Long userId;
    private String userName;
    private String userEmail;
    private List<ReviewResponse> userReviews;
    private List<RestaurantResponse> userFavoriteRestaurants;

    public static UserMyPageDto of(User user, List<ReviewResponse> reviewResponses, RestaurantResponse restaurantResponse) {
        UserMyPageDto dto = new UserMyPageDto();
        dto.userId = user.getId();
        dto.userName = user.getName();
        dto.userEmail = user.getEmail();
        dto.userReviews = reviewResponses;

    }
}
