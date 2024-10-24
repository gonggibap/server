package kr.kro.gonggibap.domain.review.repository;

import kr.kro.gonggibap.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r " +
            "FROM Review r " +
            "JOIN FETCH r.images " +
            "WHERE r.restaurant.id = :restaurantId")
    List<Review> findAllByRestaurantIdWithImages(Long restaurantId);

    @Query("SELECT r " +
            "FROM Review r " +
            "JOIN FETCH r.images " +
            "WHERE r.id = :reviewId")
    Optional<Review> findByIdWithImages(Long reviewId);
}