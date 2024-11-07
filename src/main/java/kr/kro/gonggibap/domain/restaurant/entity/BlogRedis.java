package kr.kro.gonggibap.domain.restaurant.entity;

import java.util.List;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.annotation.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import kr.kro.gonggibap.domain.restaurant.dto.BlogPostDto;

@Getter
@RedisHash(value = "blog", timeToLive = 3600 * 24) // 1일
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 기본 생성자
@AllArgsConstructor(access = AccessLevel.PUBLIC)    // 모든 필드 생성자
public class BlogRedis {
    @Id // 수정된 부분
    private String restaurantId;
    private List<BlogPostDto> documents;
}
