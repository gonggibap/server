package kr.kro.gonggibap.domain.restaurant.service;

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
    private final UserService userService;
    private final ReviewService reviewService;
    private final HistoryService historyService;

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

    public List<RestaurantResponse> getFavoriteList(final User user, final Pageable pageable) {
        Page<FavoriteRestaurant> favorites = favoriteRestaurantRepository.findByUser(user, pageable);

        // Restaurant IDs 수집
        List<Long> restaurantIds = favorites.stream()
                .map(favorite -> favorite.getRestaurant().getId())
                .toList();

        // 2단계: 추가로 필요한 History와 Review 데이터 로드
        List<History> histories = historyService.bulkByRestaurantIds(restaurantIds);
        List<Review> reviews = reviewService.bulkByRestaurantIds(restaurantIds);

        // History와 Review를 Restaurant ID 기준으로 매핑
        Map<Long, List<History>> historyMap = histories.stream()
                .collect(Collectors.groupingBy(history -> history.getRestaurant().getId()));

        Map<Long, Double> pointAvgMap = reviews.stream()
                .collect(Collectors.groupingBy(review -> review.getRestaurant().getId(),
                        Collectors.averagingDouble(Review::getPoint)));

        // 3단계: 결과 매핑
        return favorites.map(favorite -> {
            Restaurant restaurant = favorite.getRestaurant();

            Long visitCount = historyMap.getOrDefault(restaurant.getId(), Collections.emptyList()).size();
            Double pointAvg = pointAvgMap.getOrDefault(restaurant.getId(), 0.0);


        }
    }
