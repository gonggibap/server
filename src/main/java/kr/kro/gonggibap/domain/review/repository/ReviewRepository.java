package kr.kro.gonggibap.domain.review.repository;

import kr.kro.gonggibap.domain.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r " +
            "FROM Review r " +
            "LEFT JOIN FETCH r.images " +
            "JOIN FETCH r.user " +
            "WHERE r.restaurant.id = :restaurantId " +
            "ORDER BY r.createdDate DESC")
    Page<Review> findAllByRestaurantIdWithImages(Long restaurantId, Pageable pageable);

    @Query("SELECT r " +
            "FROM Review r " +
            "LEFT JOIN FETCH r.images " +
            "WHERE r.id = :reviewId")
    Optional<Review> findByIdWithImages(Long reviewId);

    @Query("SELECT r " +
            "FROM Review r " +
            "LEFT JOIN FETCH r.images " +
            "WHERE r.user.id = :userId " +
            "ORDER BY r.createdDate DESC")
    List<Review> findPageByUserIdWithImages(@Param("userId") Long userId, Pageable pageable);

}
