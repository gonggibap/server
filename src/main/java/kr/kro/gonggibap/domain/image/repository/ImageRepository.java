package kr.kro.gonggibap.domain.image.repository;

import kr.kro.gonggibap.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {


    @Query("SELECT i FROM Image i WHERE i.review.id = :reviewId")
    List<Image> findByReviewId(@Param("reviewId") Long reviewId);
}
