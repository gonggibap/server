package kr.kro.gonggibap.core.util;

import kr.kro.gonggibap.core.config.jwt.constant.GrantType;
import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.exception.BusinessException;
import org.springframework.util.StringUtils;

public class AuthorizationUtils {

    public static void validateAuthorization(String header) {

        if (!StringUtils.hasText(header))
            throw new BusinessException(ErrorCode.NOT_EXISTS_AUTHORIZATION);

        String[] authHeader = header.split(" ");

        if (authHeader.length < 2 || (!GrantType.BEARER.getType().equals(authHeader[0])))
            throw new BusinessException(ErrorCode.NOT_VALID_BEARER_GRANT_TYPE);
    }
}
