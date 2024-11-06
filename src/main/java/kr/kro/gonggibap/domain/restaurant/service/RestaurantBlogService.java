package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.dto.BlogPost; // BlogPost import
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantBlogResponse;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import kr.kro.gonggibap.domain.restaurant.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static kr.kro.gonggibap.core.error.ErrorCode.NOT_FOUND_RESTAURANT;

@Slf4j
@RequiredArgsConstructor
@Service


/**
 * 카카오 API를 사용해 블로그 포스트 가져옴
 *
 * @param restaurantId
 * @return RestaurantPageResponse response
 */
public class RestaurantBlogService {
    private static final String KAKAO_SEARCH_BLOG_URL = "https://dapi.kakao.com/v2/search/blog";
    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String apiKey;

    public List<BlogPost> searchBlogPostWithAPI(Long restaurantId) { // 반환 타입 수정

        // ID를 가지고 restaurant 추출
        Restaurant r = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_RESTAURANT));

        String restaurantQuery = String.join("", r.getRestaurantName(), r.getAddressName());
//        log.info("name: {}, address:{}", r.getRestaurantName(), r.getAddressName());

// URL 구성
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(KAKAO_SEARCH_BLOG_URL)
                .queryParam("query", restaurantQuery)
                .queryParam("sort", "accuracy")
                .queryParam("size", 3); // 최대 3개의 결과만 가져옴

// URI 객체 생성
        URI uri = builder.build().encode().toUri();

// Header 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + apiKey);

// HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

// 요청 전송
        ResponseEntity<RestaurantBlogResponse> response = restTemplate.exchange(
                uri, HttpMethod.GET, entity, RestaurantBlogResponse.class
        );
        // documents만 추출하여 반환
        return response.getBody().getDocuments();
    }
}
