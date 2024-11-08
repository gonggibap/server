package kr.kro.gonggibap.domain.review.service;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.error.PageResponse;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.image.entity.Image;
import kr.kro.gonggibap.domain.image.repository.ImageRepository;
import kr.kro.gonggibap.domain.image.service.ImageS3UploadService;
import kr.kro.gonggibap.domain.image.service.ImageService;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import kr.kro.gonggibap.domain.restaurant.service.RestaurantService;
import kr.kro.gonggibap.domain.review.dto.request.ReviewCreateRequest;
import kr.kro.gonggibap.domain.review.dto.request.ReviewUpdateRequest;
import kr.kro.gonggibap.domain.review.dto.response.ReviewResponse;
import kr.kro.gonggibap.domain.review.entity.Review;
import kr.kro.gonggibap.domain.review.repository.ReviewRepository;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static kr.kro.gonggibap.domain.review.service.helper.ReviewConverter.getMyReviewResponses;
import static kr.kro.gonggibap.domain.review.service.helper.ReviewConverter.getReviewResponses;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ImageS3UploadService imageS3UploadService;
    private final ImageService imageService;
    private final RestaurantService restaurantService;
    private final ReviewRepository reviewRepository;
    private final ImageRepository imageRepository;

    /**
     * 리뷰 작성 메소드
     * 리뷰 작성시 이미지는 S3에 업로드
     * 사용자에게는 이미지 링크만 반환
     *
     * @param request
     * @param user
     * @return
     */
    @Transactional
    public Long createReview(ReviewCreateRequest request, Long restaurantId, User user) {
        Restaurant findRestaurant = restaurantService.findRestaurantById(restaurantId);
        Review savedReview = reviewRepository.save(new Review(request.getContent(), request.getPoint(), user, findRestaurant));
        List<MultipartFile> images = request.getImages();

        // 첨부된 여러 이미지가 있을 경우 S3에 한 번의 요청으로 업로드
        if (!CollectionUtils.isEmpty(images)) {
            List<String> imageUrls = imageS3UploadService.saveReviewFiles(images);
            imageService.createImages(savedReview, imageUrls);
        }

        // 사용자 리뷰 점수 증가
        user.increaseScore();

        return savedReview.getId();
    }

    /**
     * 리뷰 수정 메소드
     * S3에 등록되어있는 이미지 중 현재 request에 들어온 이미지가 아닌 이미지는 삭제
     * 새로운 이미지 S3에 등록
     * images 테이블에 있는 기존 이미지 삭제 후 새로운 이미지 등록
     *
     * @param request
     * @param reviewId
     * @param user
     * @return
     */
    @Transactional
    public Long updateReview(ReviewUpdateRequest request, Long reviewId, User user) {

        // reviewId로 기존 리뷰 조회
        Review findByReviewId = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        // 권한 확인
        findByReviewId.validatePermission(user.getId());

        // reviewId로 imageRepository에서 image 조회
        List<Image> findImageByReviewId = imageRepository.findByReviewId(reviewId);

        // 기존 이미지의 URL 리스트 추출
        List<String> existingImageUrls = findImageByReviewId.stream()
                .map(Image::getImageUrl)
                .toList();

        // S3에 올라와 있는 해당 이미지(image_url로 확인)를 삭제
        if (!CollectionUtils.isEmpty(existingImageUrls)) {
            imageS3UploadService.deleteReviewFiles(existingImageUrls);
        }

        // images테이블에서 reviewId로 검색되는 칼럼 다 삭제
        imageRepository.deleteAll(findImageByReviewId);

        // 리뷰 수정 내용 반영
        findByReviewId.updateReview(request.getContent(), request.getPoint());

        // 수정 시 첨부한 이미지가 있을 시 S3에 새로 업로드
        List<MultipartFile> images = request.getImages();
        if (!CollectionUtils.isEmpty(images)) {
            List<String> newImageUrls = imageS3UploadService.saveReviewFiles(images);
            imageService.createImages(findByReviewId, newImageUrls);
        }

        return findByReviewId.getId();
    }


    /**
     * 특정 식당에 대한 리뷰 전체 조회 메소드
     *
     * @param restaurantId
     * @return
     */
    public PageResponse getReviews(Long restaurantId, Pageable pageable) {
        restaurantService.findRestaurantById(restaurantId);

        Page<Review> reviewPages = reviewRepository.findAllByRestaurantIdWithImages(restaurantId, pageable);


        List<ReviewResponse> reviews = getReviewResponses(reviewPages.getContent());

        return new PageResponse(reviewPages.getTotalPages(), reviews);
    }

    /**
     * 리뷰 삭제 메소드
     * 삭제 시 관련된 이미지 제거
     *
     * @param user
     * @param reviewId
     */
    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review review = reviewRepository.findByIdWithImages(reviewId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        review.validatePermission(user.getId());

        // 리뷰에 연결된 모든 이미지 URL 추출
        List<Image> images = review.getImages();
        List<String> imageUrls = images.stream()
                .map(Image::getImageUrl)
                .toList();

        // S3에서 여러 이미지 URL을 한 번에 삭제
        if (!CollectionUtils.isEmpty(imageUrls)) {
            imageS3UploadService.deleteReviewFiles(imageUrls);
        }

        reviewRepository.deleteById(reviewId);

        // 사용자 리뷰 점수 감소
        user.decreaseScore();
    }

    /**
     * 사용자가 작성한 리뷰를 조회하는 메소드
     * 페이징 처리가 가능하다
     *
     * @param user
     * @param pageable
     * @return
     */
    public List<ReviewResponse> getMyPagingReviews(User user, Pageable pageable) {
        List<Review> reviews = reviewRepository.findPageByUserIdWithImages(user.getId(), pageable);
        return getMyReviewResponses(user, reviews);
    }

}
