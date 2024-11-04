package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.entity.RestaurantUrl;
import kr.kro.gonggibap.domain.restaurant.repository.RestaurantUrlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RestaurantUrlService {
    private final RestaurantUrlRepository restaurantUrlRepository;

    @Value(value = "${shorturl.base62}")
    private String base62;

    private static final int SHORT_CODE_LENGTH = 8;

    @Transactional
    public String createShortUrl(String originalUrl) {
        String shortCode;
        Optional<RestaurantUrl> existingShortUrl;

        do {
            shortCode = generateShortUrl();
            existingShortUrl = restaurantUrlRepository.findByShortUrl(shortCode);
        } while (existingShortUrl.isPresent());

        RestaurantUrl shortUrl = RestaurantUrl.builder()
                .originalUrl(originalUrl)
                .shortUrl(shortCode)
                .createdAt(new Date())
                .build();

        restaurantUrlRepository.save(shortUrl);
        return shortCode;
    }

    public String getOriginalUrl(String shortUrl) {
        RestaurantUrl restaurantUrl = restaurantUrlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_SHORT_URL));

        return restaurantUrl.getOriginalUrl();
    }

    private String generateShortUrl() {
        Random random = new Random();
        StringBuilder shortCode = new StringBuilder();
        for (int i = 0; i < SHORT_CODE_LENGTH; i++) {
            shortCode.append(base62.charAt(random.nextInt(base62.length())));
        }
        return shortCode.toString();
    }
}
