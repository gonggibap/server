package kr.kro.gonggibap.domain.image.service;

import kr.kro.gonggibap.domain.image.entity.Image;
import kr.kro.gonggibap.domain.image.repository.ImageRepository;
import kr.kro.gonggibap.domain.review.entity.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepository imageRepository;

    @Transactional
    public void createImages(Review review, List<String> imageUrls){

        List<Image> images = imageUrls.stream()
                .map(url -> new Image(review, url))
                .toList();

        imageRepository.saveAll(images);
    }
}
