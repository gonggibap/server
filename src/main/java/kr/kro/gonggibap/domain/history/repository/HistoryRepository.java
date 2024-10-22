package kr.kro.gonggibap.domain.history.repository;

import kr.kro.gonggibap.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
