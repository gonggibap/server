package kr.kro.gonggibap.domain.restaurant.repository;

import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchResponse;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse(r.id, r.restaurantName, r.link, r.category, r.address, r.roadAddress, r.latitude, r.longitude, h.publicOffice.id, COUNT(h)) " +
            "FROM Restaurant r " +
            "LEFT JOIN r.histories h " +
            "WHERE FUNCTION('ST_Contains', FUNCTION('ST_GeomFromText', :polygon, 4326), r.location) = true " +
            "GROUP BY r.id " +
            "ORDER BY COUNT(h) desc")
    Page<RestaurantResponse> getRestaurant(String polygon, Pageable pageable);

    /**
     * N-gram 기반 fulltext index를 restaurants 이름 기준으로 검색
     */
    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchResponse(r.id, r.restaurantName, r.link, r.category, r.address, r.roadAddress, r.latitude, r.longitude ) " +
            "FROM Restaurant r " +
            "WHERE FUNCTION('match_against', r.restaurantName, :query) > 0 " +  // Check if the relevance score is positive
            "ORDER BY FUNCTION('match_against', r.restaurantName, :query) DESC")
    Page<RestaurantSearchResponse> searchRestaurant(String query, Pageable pageable);

}
