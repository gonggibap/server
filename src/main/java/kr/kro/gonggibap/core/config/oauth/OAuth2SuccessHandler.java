package kr.kro.gonggibap.core.config.oauth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.kro.gonggibap.core.config.jwt.TokenProvider;
import kr.kro.gonggibap.core.config.jwt.constant.TokenType;
import kr.kro.gonggibap.core.util.CookieUtil;
import kr.kro.gonggibap.domain.user.dto.CustomOAuth2User;
import kr.kro.gonggibap.domain.user.dto.TokenDetails;
import kr.kro.gonggibap.domain.user.entity.User;
import kr.kro.gonggibap.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // 스프링 시큐리티에서 별도의 authenticationSuccessHandler를 지정하지 않으면
    // 로그인 성공 이후 SimpleUrlAuthenticationSuccessHandler 사용한다.

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final String ACCESS_TOKEN_COOKIE_NAME = "access_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(30);

    // 로그인 성공 시 리다이렉트 페이지
    private final String redirectPath;
    private final TokenProvider tokenProvider;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserService userService;

    public OAuth2SuccessHandler(
            TokenProvider tokenProvider,
            OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository,
            UserService userService,
            @Value("${auth.redirect-path}") String redirectPath) {
        this.tokenProvider = tokenProvider;
        this.authorizationRequestRepository = authorizationRequestRepository;
        this.userService = userService;
        this.redirectPath = redirectPath;
    }

    // 성공적으로 로그인 하는 경우에 토큰과 관련된 작업을 추가로 처리하기 위해 오버라이드함
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//        // 인증된 principal(주체)를 반환한다.
//        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
//        System.out.println("여기 확인하자");
//        System.out.println(oAuth2User);
//        String email = ((Map<String, String>) oAuth2User.getAttributes().get("kakao_account")).get("email");
//        User user = userService.findByEmail(email);
//    }

    // 일반적인 로직은 동일하게 사용하고, 토큰과 관련된 작업만 추가로 처리하기 위해 오버라이드함
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println(authentication);
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        log.info("oauth login user: {}", oAuth2User);
        log.info("oauth login user name: {}", oAuth2User.getName());
        log.info("oauth login user attributes: {}", oAuth2User.getAttributes());

        String email = ((CustomOAuth2User) oAuth2User).getUserDto()
                .getEmail();
        System.out.println(email);

        User user = userService.findByEmail(email);

        // 1. 리프레시 토큰 생성 -> 저장 -> 쿠키에 저장
        TokenDetails refreshTokenDetails = tokenProvider.generateToken(user, REFRESH_TOKEN_DURATION, TokenType.REFRESH);
        String refreshToken = refreshTokenDetails.token();
        // 데이터 베이스에 유저아이디와 리프레시 토큰을 저장
        //saveRefreshToken(user, refreshToken);

        // 클라이언트에서 액세스 토큰이 만료되면 재발급 요청하도록 쿠키에 리프레시 토큰을 저장
        addRefreshTokenToCookie(request, response, refreshToken);       // 2. 액세스 토큰 생성 -> 패스에 액세스 토큰을 추가

        // 토큰 제공자를 사용해 액세스 토큰을 만든 뒤
        TokenDetails accessTokenDetails = tokenProvider.generateToken(user, ACCESS_TOKEN_DURATION, TokenType.ACCESS);
        String accessToken = accessTokenDetails.token();

        // 쿠키에 액세스 토큰을 저장
        addAccessTokenToCookie(request, response, accessToken);

        // 쿠키에서 리다이렉트 경로가 담긴 값을 가져와 쿼리파라미터에 액세스 토큰을 추가함
        String targetUrl = getTargetUrl(
                accessToken,
                accessTokenDetails.expireTime(),
                refreshToken,
                refreshTokenDetails.expireTime()
        );

        // 인증 관련 설정값, 쿠키 제거
        clearAuthenticationAttributes(request, response);

        //리다이렉트 ( 2에서 만든 URL로 리다이렉트합니다)
        log.info("targetUrl: {}", targetUrl);
        log.info("accessToken: {}", accessToken);
        log.info("authentication: {}", authentication);

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private void addAccessTokenToCookie(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        int cookieMaxAge = (int) ACCESS_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, ACCESS_TOKEN_COOKIE_NAME, accessToken, cookieMaxAge);
    }

    // 생성된 리프레시 토큰을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        // Oauth 인증을 위해 저장된 정보도 삭제
//        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // 액세스 토큰을 패스에 추가
    private String getTargetUrl(String accessToken, LocalDateTime accessTokenExpireTime, String refreshToken, LocalDateTime refreshTokenExpireTime) {
        return UriComponentsBuilder.fromUriString(redirectPath)
                .queryParam("accessToken", accessToken)
                .queryParam("accessTokenExpireTime", accessTokenExpireTime)
                .queryParam("refreshToken", refreshToken)
                .queryParam("refreshTokenExpireTime", refreshTokenExpireTime)
                .build()
                .toUriString();
    }
}