package kr.kro.gonggibap.domain.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantBlogResponse {
    // 메타 정보를 담는 클래스
    private Meta meta; // 메타 정보 필드
    private List<BlogPost> documents; // 블로그 포스트 리스트

    // Meta 클래스를 정의
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Meta {
        private int total_count; // 총 개수
        private int pageable_count; // 페이지 가능 개수
        private boolean is_end; // 끝 여부
    }

    // BlogPost 클래스를 정의
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BlogPost {
        private String title; // 제목
        private String contents; // 내용
        private String url; // URL
        private String blogname; // 블로그 이름
        private String thumbnail; // 썸네일
        private String datetime; // 작성 시간
    }
}
