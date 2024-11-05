package kr.kro.gonggibap.domain.restaurant.repository;

import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.entity.FavoriteRestaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FavoriteRestaurantRepository extends JpaRepository<FavoriteRestaurant, Long> {
    @Query("SELECT fr FROM FavoriteRestaurant fr " +
            "JOIN FETCH fr.restaurant r " +
            "WHERE r.id = :restaurantId")
    Optional<FavoriteRestaurant> findByRestaurantId(@Param("restaurantId") Long restaurantId);

    @Modifying
    @Query("DELETE FROM FavoriteRestaurant fr " +
            "WHERE fr.user.id = :userId " +
            "AND fr.restaurant.id = :restaurantId")
    void deleteFavoriteRestaurant(@Param("userId") Long userId, @Param("restaurantId") Long restaurantId);

    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse(r.id, r.restaurantName, r.phone, r.link, r.category, r.detailCategory, r.addressName, r.roadAddressName, r.latitude, r.longitude, h.publicOffice.id, p.name, COUNT(distinct h), AVG(rev.point)) " +
            "FROM FavoriteRestaurant fr " +
            "JOIN fr.restaurant r " +
            "LEFT JOIN r.histories h " +
            "LEFT JOIN r.reviews rev " +
            "LEFT JOIN h.publicOffice p " +
            "WHERE fr.user.id = :userId " +
            "GROUP BY r.id " +
            "ORDER BY COUNT(distinct h) desc")
    Page<RestaurantResponse> findByPagingUser(@Param("userId") Long userId, Pageable pageable);

    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse(r.id, r.restaurantName, r.phone, r.link, r.category, r.detailCategory, r.addressName, r.roadAddressName, r.latitude, r.longitude, h.publicOffice.id, p.name, COUNT(distinct h), AVG(rev.point)) " +
            "FROM FavoriteRestaurant fr " +
            "JOIN fr.restaurant r " +
            "LEFT JOIN r.histories h " +
            "LEFT JOIN r.reviews rev " +
            "LEFT JOIN h.publicOffice p " +
            "WHERE fr.user.id = :userId " +
            "GROUP BY r.id " +
            "ORDER BY COUNT(distinct h) desc")
    Slice<RestaurantResponse> findBySlicingUser(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT COUNT(f) > 0 " +
            "FROM FavoriteRestaurant f " +
            "WHERE f.user.id = :userId " +
            "AND f.restaurant.id = :restaurantId")
    boolean existsByUserAndRestaurant(@Param("userId") Long userId, @Param("restaurantId") Long restaurantId);

    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse(r.id, r.restaurantName, r.phone, r.link, r.category, r.detailCategory, r.addressName, r.roadAddressName, r.latitude, r.longitude, h.publicOffice.id, p.name, COUNT(distinct h), AVG(rev.point)) " +
            "FROM FavoriteRestaurant fr " +
            "JOIN fr.restaurant r " +
            "LEFT JOIN r.histories h " +
            "LEFT JOIN r.reviews rev " +
            "LEFT JOIN h.publicOffice p " +
            "WHERE fr.user.id = :userId " +
            "and fr.restaurant.detailCategory = :category " +
            "GROUP BY r.id " +
            "ORDER BY COUNT(distinct h) desc")
    Page<RestaurantResponse> findByCategoryPagingUser(@Param("userId") Long userId,
                                                      @Param("category") String category,
                                                      Pageable pageable);
}
