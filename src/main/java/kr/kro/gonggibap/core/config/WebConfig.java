package kr.kro.gonggibap.core.config;

import kr.kro.gonggibap.core.resolver.CurrentUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUserArgumentResolver currentUserArgumentResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
//                .allowedOrigins("*")
//                .allowCredentials(false)
                .allowedOrigins("http://localhost:3000",
                                "https://gonggibap.co.kr"
                        ) // 프론트엔드 주소
                .allowCredentials(true)  // 인증 정보 허용, origin 모두 허용인 경우 인증 정보 허용하지 않음
                .allowedHeaders("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS")
                .maxAge(3600);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {

        resolvers.add(currentUserArgumentResolver);
    }

}
