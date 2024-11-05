package kr.kro.gonggibap.core.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kr.kro.gonggibap.core.config.jwt.constant.TokenType;
import kr.kro.gonggibap.domain.user.dto.TokenDetails;
import kr.kro.gonggibap.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Service
public class TokenProvider {
    private final static String TOKEN_PREFIX = "Bearer ";

    private final JwtProperties jwtProperties;

    public TokenDetails generateToken(User user, Duration expiredAt, TokenType tokenType) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiredAt.toMillis());
        String token = makeToken(expiryDate, user, tokenType);

        LocalDateTime expiryDateTime = expiryDate.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();

        return new TokenDetails(token, expiryDateTime);
    }

    private String makeToken(Date expiryDate, User user, TokenType tokenType) {

        return makeAccessToken(expiryDate, user);
    }

    // JWT 토큰 생성 메서드
    private String makeAccessToken(Date expiry, User user) {

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 타입은 JWT
                .setHeaderParam(TokenType.ACCESS.name(), TokenType.ACCESS.name())
                .setIssuer(jwtProperties.getIssuer()) // 내용 : 프로퍼티에스에서 지정한 발급자명
                .setIssuedAt(new Date(System.currentTimeMillis())) // 내용 issue at : 현재 시간
                .setExpiration(expiry) // 내용 exp : expiry 멤버 변수값
                .setSubject(user.getEmail()) // 내용 sub : 유저의 이메일
                .claim("id", user.getId()) // 클레임 id : 유저 id
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey()
                        .getBytes(StandardCharsets.UTF_8)) // 서명 추가
                .compact();
    }

    // JWT 토큰 유효성 검증메서드
    public boolean validToken(String token) {
        try {

            log.info("validate token password: {}", jwtProperties.getSecretKey());

            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)) // 서명 검증
                    .parseClaimsJws(token); // 클레임이란 받아온 정보(토큰)를 jwt 페이로드에 넣는 것이다.

            log.info("secret key: {}", jwtProperties.getSecretKey());

            return true;
        } catch (Exception e) {
            log.info("failed to validate token: {}", e.getMessage());
            return false;
        }
    }

    // 토큰 기반으로 스프링 시큐리티 인증 정보를 가져오는 메서드
    public Authentication getAuthentication(String token) {
        // 반환값은 스프링 시큐리티 인증이다.
        // 페이로드에 저장된 클레임 정보를 반환 받는다.
        Claims claims = getClaims(token);

        // ROLE_USER의 권환들
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        // 스프링 시큐리티 사용자 인증을 반환한다.
        // 사용자 이름과 비밀번호를 이용한 인증을 진행한다. JWT 클레임에서 JWT가 발급된 사용자를 가져온다.
        return new UsernamePasswordAuthenticationToken(
                // 시큐리티 유저 정보에 클레임에서 받아온 유저 이메일 정보, 비밀번호, 권한을 넣고 생성, 토큰, 권한목록을 통해 인증 정보 반환
                new org.springframework.security.core.userdetails.User(claims.getSubject(), "", authorities),
                token, authorities);
    }

    public Long getUserId(String token) {
        Claims claims = getClaims(token);

        return claims.get("id", Long.class);
    }

    // 토큰을 분석하면서 claims을 빼낸다.
    public Claims getClaims(String token) {
        return Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)) // 서명 검증
                .parseClaimsJws(token)
                .getBody();
    }

    public String getAccessToken(String authorzationHeader) {
        // 토큰의 접두사를 제거하고 값을 돌려준다.
        if (authorzationHeader != null && authorzationHeader.startsWith(TOKEN_PREFIX)) {
            return authorzationHeader.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}


