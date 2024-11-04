package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.core.error.ErrorCode;
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

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteRestaurantService {
    private final FavoriteRestaurantRepository favoriteRestaurantRepository;
    private final RestaurantService restaurantService;

    /**
     * 로그인 한 사용자가 식당 ID를 통해 좋아요 로직을 수행함
     * 만약 이미 좋아요한 식당인 경우 409 Conflict 발생
     *
     * @param user
     * @param restaurantId
     */
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

    /**
     * 로그인 한 사용자가 특정 식당의 좋아요를 취소하는 메소드
     * 만약, 기존에 좋아요를 하지 않은 경우에는 404 에러 발생
     *
     * @param user
     * @param restaurantId
     */
    @Transactional
    public void unFavoriteRestaurant(User user, Long restaurantId) {
        if (!favoriteRestaurantRepository.existsByUserAndRestaurant(user.getId(), restaurantId)) {
            throw new CustomException(ErrorCode.NOT_FOUND_FAVORITE_RESTAURANT);
        }
        favoriteRestaurantRepository.deleteFavoriteRestaurant(user.getId(), restaurantId);
    }

    /**
     * 로그인 한 사용자가 좋아요한 식당 목록을 조회하는 메소드
     * 페이징 처리가 되어, count 쿼리가 한번 더 나감
     *
     * @param user
     * @param pageable
     * @return
     */
    public Page<RestaurantResponse> getFavoritePagingList(final User user, final Pageable pageable) {
        return favoriteRestaurantRepository.findByPagingUser(user.getId(), pageable);
    }

    /**
     * 로그인 한 사용자가 좋아요한 식당 목록을 조회하는 메소드
     * 페이징 처리가 되지 않아, count 쿼리가 나가지 않음
     * 따라서 전체 페이지 수는 조회할 수 없음
     *
     * @param user
     * @param pageable
     * @return
     */
    public List<RestaurantResponse> getFavoriteSlicingList(final User user, final Pageable pageable) {
        return favoriteRestaurantRepository.findBySlicingUser(user.getId(), pageable).getContent();
    }

    /**
     * 특정 식당에 사용자가 좋아요를 눌렀는 지를 확인하는 메소드
     *
     * @param user
     * @param restaurantId
     * @return
     */
    public boolean isFavoriteRestaurant(User user, Long restaurantId) {
        boolean isExist = favoriteRestaurantRepository.existsByUserAndRestaurant(user.getId(), restaurantId);

        // 존재하지 않는다면
        if (!isExist) {
            // 식당 ID 값부터가 잘못된 경우
            if (!restaurantService.existsById(restaurantId)) {
                throw new CustomException(ErrorCode.NOT_FOUND_RESTAURANT);
            }
            return false;
        }
        return true;
    }
}

