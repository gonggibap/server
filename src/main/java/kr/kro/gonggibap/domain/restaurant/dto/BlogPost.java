package kr.kro.gonggibap.domain.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPost {
    private String title; // 제목
    private String contents; // 내용
    private String url; // URL

    @JsonAlias("blogname")
    @JsonProperty("name")  // JSON에서 blogname을 매핑
    private String name;       // 응답에서도 name으로 표시

    private String thumbnail; // 썸네일
    private String datetime; // 작성 시간
}
