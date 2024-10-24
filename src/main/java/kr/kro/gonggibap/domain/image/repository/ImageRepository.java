package kr.kro.gonggibap.domain.image.repository;

import kr.kro.gonggibap.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
