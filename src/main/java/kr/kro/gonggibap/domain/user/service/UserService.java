package kr.kro.gonggibap.domain.user.service;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.entity.FavoriteRestaurant;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import kr.kro.gonggibap.domain.restaurant.service.RestaurantService;
import kr.kro.gonggibap.domain.review.dto.response.ReviewResponse;
import kr.kro.gonggibap.domain.review.entity.Review;
import kr.kro.gonggibap.domain.user.dto.UserDto;
import kr.kro.gonggibap.domain.user.dto.UserMyPageDto;
import kr.kro.gonggibap.domain.user.entity.User;
import kr.kro.gonggibap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final RestaurantService restaurantService;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
    }

    public UserDto findById(final Long id) {
        Optional<User> user = userRepository.findById(id);

        // ID 값으로 유저를 찾을 수 없는 경우
        if (user.isEmpty()) {
            log.error("해당 ID({})의 유저를 찾을 수 없습니다. ", id);
            throw new CustomException(ErrorCode.USER_NOT_EXISTS);
        }

        return UserDto.of(user.get());
    }

    public UserMyPageDto getUserInfo(final User user) {
        User fetchedUser = userRepository.getUserInfo(user)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));

        List<Review> userReviews = fetchedUser.getReviews();

        List<ReviewResponse> reviewResponses = userReviews.stream()
                .map(review -> ReviewResponse.of(fetchedUser, review))
                .toList();

        List<FavoriteRestaurant> userFavorites = fetchedUser.getFavorites();

        List<Restaurant> restaurants = userFavorites.stream()
                .map(favoriteRestaurant -> restaurantService.findRestaurantById(favoriteRestaurant.getRestaurant().getId()))
                .toList();


        UserMyPageDto.of(fetchedUser, reviewResponses, )


    }
}
