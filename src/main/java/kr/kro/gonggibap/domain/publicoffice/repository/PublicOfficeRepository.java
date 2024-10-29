package kr.kro.gonggibap.domain.publicoffice.repository;

import kr.kro.gonggibap.domain.publicoffice.entity.PublicOffice;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PublicOfficeRepository extends JpaRepository<PublicOffice, Long> {

    @Query(value = "SELECT new kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantResponse(r.id, r.restaurantName, r.phone, r.link, r.category, r.detailCategory, r.addressName, r.roadAddressName, r.latitude, r.longitude, h.publicOffice.id, h.publicOffice.name, COUNT(h), AVG(rev.point)) " +
            "FROM History h " +
            "JOIN h.restaurant r " +
            "LEFT JOIN r.reviews rev " +
            "WHERE h.publicOffice.id = :publicOfficeId " +
            "GROUP BY h.restaurant.id " +
            "ORDER BY COUNT(h) desc")
    List<RestaurantResponse> findRestaurantsByPublicOfficeId(@Param("publicOfficeId") Long publicOfficeId);
}
