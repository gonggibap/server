package kr.kro.gonggibap.domain.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BlogPost {
    private String title; // 제목
    private String contents; // 내용
    private String url; // URL
    private String blogname; // 블로그 이름
    private String thumbnail; // 썸네일
    private String datetime; // 작성 시간
}
