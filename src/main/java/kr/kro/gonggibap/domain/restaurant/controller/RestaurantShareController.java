package kr.kro.gonggibap.domain.restaurant.controller;

import kr.kro.gonggibap.core.error.CommonResponse;
import kr.kro.gonggibap.domain.restaurant.service.RestaurantUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.kro.gonggibap.core.error.CommonResponse.*;

@RestController
@RequestMapping("/api/shorts")
@RequiredArgsConstructor
public class RestaurantShareController {
    private final RestaurantUrlService urlService;

    @PostMapping("/create")
    public ResponseEntity<?> createShareLink(@RequestParam String originalUrl) {
        String shortCode = urlService.createShortUrl(originalUrl);
        return ResponseEntity.ok(success(shortCode));
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> getOriginalUrl(@PathVariable String shortUrl) {
        String originalUrl = urlService.getOriginalUrl(shortUrl);
        return ResponseEntity.ok(success(originalUrl));
    }

}
