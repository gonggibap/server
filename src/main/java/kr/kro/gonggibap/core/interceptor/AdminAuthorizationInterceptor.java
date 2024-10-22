package kr.kro.gonggibap.core.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.kro.gonggibap.core.config.jwt.TokenProvider;
import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.CustomException;
import kr.kro.gonggibap.domain.user.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AdminAuthorizationInterceptor implements HandlerInterceptor {

    private final TokenProvider tokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getMethod().equals("OPTIONS"))
            return true;

        String authorization = request.getHeader("Authorization");
        String accessToken = authorization.split(" ")[1];

        Claims claims = tokenProvider.getClaims(accessToken);
        String role = (String) claims.get("role");

        if (!UserRole.isUserRole(role))
            throw new CustomException(ErrorCode.FORBIDDEN_ADMIN);

        return true;
    }
}
