package kr.kro.gonggibap.domain.restaurant.repository;

import kr.kro.gonggibap.domain.restaurant.entity.FavoriteRestaurant;
import kr.kro.gonggibap.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {
    @Query("SELECT fr FROM FavoriteRestaurant fr " +
            "JOIN FETCH fr.restaurant r " +
            "WHERE r.id = :restaurantId")
    Optional<FavoriteRestaurant> findByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Query("DELETE FROM FavoriteRestaurant fr " +
            "WHERE fr.user = :user " +
            "AND fr.restaurant.id = :restaurantId")
    void deleteFavoriteRestaurant(@Param("user") User user, @Param("restaurantId") Long restaurantId);

    @EntityGraph(attributePaths = {
            "restaurant"})
    Page<FavoriteRestaurant> findByUser(User user, Pageable pageable);

}
