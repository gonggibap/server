package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.entity.FavoriteRestaurant;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import kr.kro.gonggibap.domain.restaurant.repository.FavoriteRestaurantRepository;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteRestaurantService {
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;
    private final RestaurantService restaurantService;

    @Transactional
    public void doFavoriteRestaurant(User user, Long restaurantId) {
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);

        // 중복된 식당일 시 conflict 예외 발생
        if (favoriteRestaurantRepository.existsByUserAndRestaurant(user.getId(), findRestaurant.getId())) {
            throw new CustomException(ErrorCode.DUPLICATED_FAVORITE_RESTAURANT);
        }

        FavoriteRestaurant favoriteRestaurant = FavoriteRestaurant.builder()
                .user(user)
                .restaurant(findRestaurant)
                .build();

        favoriteRestaurantRepository.save(favoriteRestaurant);
    }

    @Transactional
    public void unFavoriteRestaurant(User user, Long restaurantId) {
        if (!favoriteRestaurantRepository.existsByUserAndRestaurant(user.getId(), restaurantId)) {
            throw new CustomException(ErrorCode.NOT_FOUND_FAVORITE_RESTAURANT);
        }
        favoriteRestaurantRepository.deleteFavoriteRestaurant(user.getId(), restaurantId);
    }

    public PageResponse<?> getFavoriteList(final User user, final Pageable pageable) {
        Page<RestaurantResponse> favorites = favoriteRestaurantRepository.findByUser(user.getId(), pageable);
        return new PageResponse<>(favorites.getTotalPages(), favorites.getContent());
    }
}

