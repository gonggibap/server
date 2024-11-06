package kr.kro.gonggibap.domain.restaurant.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.kro.gonggibap.domain.restaurant.dto.BlogPost;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantBlogResponse {

    private Meta meta; // 메타 정보 필드
    private List<BlogPost> documents; // BlogPost 리스트

    // 카카오 API 메타데이터
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {

        private int totalCount; // 총 개수
        private int pageableCount; // 페이지 가능 개수
        private boolean isEnd; // 끝 여부
    }
}
