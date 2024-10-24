package kr.kro.gonggibap.domain.review.service;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.image.entity.Image;
import kr.kro.gonggibap.domain.image.service.ImageS3UploadService;
import kr.kro.gonggibap.domain.image.service.ImageService;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import kr.kro.gonggibap.domain.restaurant.service.RestaurantService;
import kr.kro.gonggibap.domain.review.dto.request.ReviewCreateRequest;
import kr.kro.gonggibap.domain.review.dto.response.ReviewResponse;
import kr.kro.gonggibap.domain.review.entity.Review;
import kr.kro.gonggibap.domain.review.repository.ReviewRepository;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ImageS3UploadService imageS3UploadService;
    private final ImageService imageService;
    private final RestaurantService restaurantService;
    private final ReviewRepository reviewRepository;

    /**
     * 리뷰 작성 메소드
     * 리뷰 작성시 이미지는 S3에 업로드
     * 사용자에게는 이미지 링크만 반환
     * @param request
     * @param user
     * @return
     */
    @Transactional
    public Long createReview(ReviewCreateRequest request, User user) {
        Restaurant findRestaurant = restaurantService.findRestaurantById(request.getRestaurantId());
        Review savedReview = reviewRepository.save(new Review(request.getContent(), request.getPoint(), user, findRestaurant));
        List<MultipartFile> images = request.getImages();

        List<String> imageUrls = images.stream()
                .map(image -> {
                    try {
                        return imageS3UploadService.saveReviewFile(image);
                    } catch (IOException e) {
                        throw new CustomException(ErrorCode.FILE_UPLOAD_ERROR);
                    }
                }).toList();

        imageService.createImages(savedReview, imageUrls);

        return savedReview.getId();
    }

    /**
     * 특정 식당에 대한 리뷰 전체 조회 메소드
     * @param restaurantId
     * @return
     */
    public List<ReviewResponse> getReviews(Long restaurantId) {
        List<Review> reviews = reviewRepository.findAllByRestaurantIdWithImages(restaurantId);

        return reviews.stream()
                .map(review -> new ReviewResponse(
                        review.getId(),
                        review.getUser().getId(),
                        review.getUser().getName(),
                        review.getPoint(),
                        review.getContent(),
                        review.getLastModifiedDate(),
                        review.getImages().stream()
                                .map(image -> image.getImageUrl()).toList()
                )).toList();
    }

    /**
     * 리뷰 삭제 메소드
     * 삭제 시 관련된 이미지 제거
     * @param user
     * @param reviewId
     */
    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review review = reviewRepository.findByIdWithImages(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        review.validatePermission(user.getId());

        List<Image> images = review.getImages();
        images.forEach(image -> imageS3UploadService.deleteReviewFile(image.getImageUrl()));

        reviewRepository.deleteById(reviewId);
    }
}
