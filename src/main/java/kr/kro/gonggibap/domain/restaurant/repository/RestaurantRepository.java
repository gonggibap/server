package kr.kro.gonggibap.domain.restaurant.repository;

import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchResponse;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse(r.id, r.restaurantName, r.link, r.category, r.addressName, r.roadAddressName, r.latitude, r.longitude, h.publicOffice.id, COUNT(h)) " +
            "FROM Restaurant r " +
            "LEFT JOIN r.histories h " +
            "WHERE FUNCTION('ST_Contains', FUNCTION('ST_GeomFromText', :polygon, 4326), r.location) = true " +
            "GROUP BY r.id " +
            "ORDER BY COUNT(h) desc")
    Page<RestaurantResponse> getRestaurant(String polygon, Pageable pageable);

    /**
     * N-gram 기반 fulltext index를 restaurants food기반으로 검색
     */
    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchResponse(r.id, r.restaurantName, r.link, r.category, r.address, r.roadAddress, r.latitude, r.longitude ) " +
            "FROM Restaurant r " +
            "WHERE FUNCTION('match_against', r.restaurantName, :food) > 0 " +  // Check if the relevance score is positive
            "ORDER BY FUNCTION('match_against', r.restaurantName, :food) DESC")
    Page<RestaurantSearchResponse> searchRestaurantByFood(String food, Pageable pageable);

    /**
     * N-gram 기반 fulltext index를 restaurants 구 기준으로 검색
     */
    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchResponse(r.id, r.restaurantName, r.link, r.category, r.address, r.roadAddress, r.latitude, r.longitude ) " +
            "FROM Restaurant r " +
            "WHERE FUNCTION('match_against', r.address, :district) > 0 " +  // Check if the relevance score is positive
            "ORDER BY FUNCTION('match_against', r.address, :district) DESC")
    Page<RestaurantSearchResponse> searchRestaurantByDistrict(String district, Pageable pageable);

    /**
     * N-gram 기반 fulltext index를 restaurants food, 구 기준으로 검색
     */
    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantSearchResponse(r.id, r.restaurantName, r.link, r.category, r.address, r.roadAddress, r.latitude, r.longitude ) " +
            "FROM Restaurant r " +
            "WHERE FUNCTION('match_against', r.restaurantName, :food) > 0 " +  // 음식 이름으로 검색
            "AND FUNCTION('match_against', r.address, :district) > 0 " +  // 주소로 검색
            "ORDER BY FUNCTION('match_against', r.restaurantName, :food) DESC, " +
            "FUNCTION('match_against', r.address, :district) DESC")
    Page<RestaurantSearchResponse> searchRestaurantByFoodAndDistrict(String food, String district, Pageable pageable);

}
