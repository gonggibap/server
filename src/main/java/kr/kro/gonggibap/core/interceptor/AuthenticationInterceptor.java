package kr.kro.gonggibap.core.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.kro.gonggibap.core.config.jwt.TokenProvider;
import kr.kro.gonggibap.core.config.jwt.constant.TokenType;
import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.core.util.AuthorizationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getMethod().equals("OPTIONS"))
            return true;

        String authorization = request.getHeader("Authorization");
        AuthorizationUtils.validateAuthorization(authorization);

        String accessToken = authorization.split(" ")[1];
        tokenProvider.validToken(accessToken);

        Claims claims = tokenProvider.getClaims(accessToken);
        String tokenType = claims.getSubject();

        if (!TokenType.isAccessToken(tokenType))
            throw new CustomException(ErrorCode.NOT_ACCESS_TOKEN_TYPE);

        return true;
    }
}
