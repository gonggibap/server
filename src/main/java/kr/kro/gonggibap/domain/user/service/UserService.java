package kr.kro.gonggibap.domain.user.service;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.service.FavoriteRestaurantService;
import kr.kro.gonggibap.domain.review.dto.response.ReviewResponse;
import kr.kro.gonggibap.domain.review.service.ReviewService;
import kr.kro.gonggibap.domain.user.dto.UserDto;
import kr.kro.gonggibap.domain.user.dto.UserMyPageDto;
import kr.kro.gonggibap.domain.user.entity.User;
import kr.kro.gonggibap.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final FavoriteRestaurantService favoriteRestaurantService;
    private final ReviewService reviewService;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXISTS));
    }

    public User findById(final Long id) {
        Optional<User> user = userRepository.findById(id);

        // ID 값으로 유저를 찾을 수 없는 경우
        if (user.isEmpty()) {
            log.error("해당 ID({})의 유저를 찾을 수 없습니다. ", id);
            throw new CustomException(ErrorCode.USER_NOT_EXISTS);
        }

        return user.get();
    }

    public UserMyPageDto getUserInfo(final User user) {
        // 리뷰를 Response 형태로 변환
        List<ReviewResponse> reviewResponses = reviewService.getMyPagingReviews(
                user, PageRequest.of(0, 5, Sort.by("createdDate").descending()));

        List<RestaurantResponse> favoritePageList = favoriteRestaurantService.getFavoriteSlicingList(user, PageRequest.of(0, 5));
        return UserMyPageDto.of(user, favoritePageList, reviewResponses);
    }
}
