package kr.kro.gonggibap.domain.restaurant.service;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.restaurant.dto.BlogPost;
import kr.kro.gonggibap.domain.restaurant.dto.response.RestaurantBlogResponse;
import kr.kro.gonggibap.domain.restaurant.entity.BlogRedis;
import kr.kro.gonggibap.domain.restaurant.entity.Restaurant;
import kr.kro.gonggibap.domain.restaurant.repository.BlogRedisRepository;
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
import java.time.Duration;
import java.util.List;

import static kr.kro.gonggibap.core.error.ErrorCode.NOT_FOUND_RESTAURANT;

@Slf4j
@RequiredArgsConstructor
@Service
public class RestaurantBlogService {
    private static final String KAKAO_SEARCH_BLOG_URL = "https://dapi.kakao.com/v2/search/blog";
    private final RestTemplate restTemplate;
    private final RestaurantRepository restaurantRepository;
    private final BlogRedisRepository blogRedisRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String apiKey;

    /**
     * KAKAO API를 활용해 블로그 글을 가져오는 로직
     * Redis를 캐시로 활용하여 API 호출을 효율적으로 진행(캐시 갱신 주기 = 1일)
     *
     * @param restaurantId
     * @return
     */
    public List<BlogPost> searchBlogPostWithAPI(Long restaurantId) {

        // Redis에서 캐시된 데이터 확인
        BlogRedis cachedBlogPosts = blogRedisRepository.findById(restaurantId.toString()).orElse(null);

        if (cachedBlogPosts != null) {
            // Redis에 캐시된 데이터가 있는 경우
            log.info("Redis에서 블로그 포스트 캐시 데이터 가져옴");
            return cachedBlogPosts.getDocuments();
        }

        // Redis에 캐시된 데이터가 없는 경우 - API 요청 수행
        Restaurant r = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new CustomException(NOT_FOUND_RESTAURANT));

        String restaurantQuery = String.join("", r.getRestaurantName(), r.getAddressName());

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

        // documents만 추출
        List<BlogPost> blogPosts = response.getBody().getDocuments();

        // Redis에 저장 (1일 동안 캐싱)
        BlogRedis blogRedis = new BlogRedis(restaurantId.toString(), blogPosts);
        blogRedisRepository.save(blogRedis);

        return blogPosts;
    }
}
