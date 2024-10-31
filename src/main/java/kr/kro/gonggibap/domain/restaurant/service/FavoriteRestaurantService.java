package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.domain.history.entity.History;
import kr.kro.gonggibap.domain.history.service.HistoryService;
import kr.kro.gonggibap.domain.publicoffice.dto.response.PublicOfficeResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.entity.FavoriteRestaurant;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import kr.kro.gonggibap.domain.restaurant.repository.FavoriteRestaurantRepository;
import kr.kro.gonggibap.domain.review.entity.Review;
import kr.kro.gonggibap.domain.review.service.ReviewService;
import kr.kro.gonggibap.domain.user.entity.User;
import kr.kro.gonggibap.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
        FavoriteRestaurant favoriteRestaurant = FavoriteRestaurant.builder()
                .user(user)
                .restaurant(findRestaurant)
                .build();
        favoriteRestaurantRepository.save(favoriteRestaurant);
    }

    @Transactional
    public void unFavoriteRestaurant(User user, Long restaurantId) {
        favoriteRestaurantRepository.deleteFavoriteRestaurant(user, restaurantId);
    }

    public PageResponse<?> getFavoriteList(final User user, final Pageable pageable) {
        Page<RestaurantResponse> favorites = favoriteRestaurantRepository.findByUser(user.getId(), pageable);
        return new PageResponse<>(favorites.getTotalPages(), favorites.getContent());
    }
}

