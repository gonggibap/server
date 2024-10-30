package kr.kro.gonggibap.domain.history.repository;

import kr.kro.gonggibap.domain.history.dto.response.HistoryResponse;
import kr.kro.gonggibap.domain.history.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    @Query("SELECT new kr.kro.gonggibap.domain.history.dto.response.HistoryResponse(" +
            "r.id, " +
            "r.restaurantName, " +
            "p.name, " +
            "h.historyDate, " +
            "h.peopleCount, " +
            "h.price, " +
            "h.useContent, " +
            "h.consumer) " +
            "FROM Restaurant r " +
            "LEFT JOIN r.histories h " +
            "LEFT JOIN h.publicOffice p " +
            "WHERE r.id = :restaurantId " +
            "ORDER BY h.historyDate DESC")
    Page<HistoryResponse> findHistoryByRestaurantId(@Param("restaurantId") Long restaurantId, Pageable pageable);

    @Query("select h " +
            "from History h " +
            "join fetch Restaurant r " +
            "where h.restaurant.id in :restaurantIds")
    List<History> bulkByRestaurantIds(@Param("restaurantIds") List<Long> restaurantIds);
}
