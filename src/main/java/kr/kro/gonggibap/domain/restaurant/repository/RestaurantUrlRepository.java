package kr.kro.gonggibap.domain.restaurant.repository;

import kr.kro.gonggibap.domain.restaurant.entity.RestaurantUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantUrlRepository extends JpaRepository<RestaurantUrl, Long> {
    Optional<RestaurantUrl> findByShortUrl(String shortUrl);
}
