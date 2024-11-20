package kr.kro.gonggibap.domain.history.repository;

import kr.kro.gonggibap.domain.history.dto.response.HistoryCountDto;
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

    @Query(value = "SELECT new kr.kro.gonggibap.domain.history.dto.response.HistoryCountDto(" +
            "h.restaurant.id, COUNT(DISTINCT h.id)) " +
            "FROM History h " +
            "WHERE h.restaurant.id IN :restaurantIds " +
            "GROUP BY h.restaurant.id")
    List<HistoryCountDto> findHistoryCounts(@Param("restaurantIds") List<Long> restaurantIds);

}
