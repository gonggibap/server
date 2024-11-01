package kr.kro.gonggibap.domain.review.repository;

import kr.kro.gonggibap.domain.review.entity.Review;
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
            "ORDER BY r.lastModifiedDate DESC")
    List<Review> findAllByRestaurantIdWithImages(Long restaurantId);

    @Query("SELECT r " +
            "FROM Review r " +
            "LEFT JOIN FETCH r.images " +
            "WHERE r.id = :reviewId")
    Optional<Review> findByIdWithImages(Long reviewId);

    List<Review> findAllByUserId(Long userId);

}
