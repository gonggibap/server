package kr.kro.gonggibap.core.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 에러입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "잘못된 입력 값입니다."),
    FILE_SIZE_EXCEED(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "최대 파일 크기는 10MB로 초과했습니다."),

    // 인증 && 인가
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "토큰이 만료되었습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 토큰은 유효한 토큰이 아닙니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "Authorization Header가 빈값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "인증 타입이 Bearer 타입이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 refresh token은 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 refresh token은 만료되었습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 토큰은 access token이 아닙니다."),
    FORBIDDEN_ADMIN(HttpStatus.FORBIDDEN, HttpStatus.FORBIDDEN.value(), "관리자가 아닙니다."),

    // 회원,
    INVALID_MEMBER_TYPE(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "잘못된 회원 타입입니다."),
    ALREADY_REGISTERED_MEMBER(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "이미 가입된 회원입니다."),
    USER_NOT_EXISTS(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 회원은 존재하지 않습니다."),
    NOT_EXISTS_EMAIL(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "일치하는 Email 정보가 존재하지 않습니다."),
    NOT_AUTHORIZATION(HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), "해당 권한이 없는 사용자입니다."),

    // 식당
    LATITUDE_COUNT_ERROR(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "4개의 위도를 입력해야 합니다."),
    LONGITUDE_COUNT_ERROR(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "4개의 경도를 입력해야 합니다."),
    COORDINATE_OUT_OF_BOUND(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "위도와 경도가 허용 범위를 벗어났습니다."),
    NOT_FOUND_RESTAURANT(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 식당은 존재하지 않습니다."),
    DONG_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 동 코드는 존재하지 않습니다."),
    // 식당 검색
    QUERY_EMPTY_ERROR(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "올바른 검색어를 입력해주세요."),

    // 리뷰
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 리뷰는 존재하지 않습니다."),

    // 공공기관
    NOT_FOUND_PUBLIC_OFFICE(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 공공기관은 존재하지 않습니다."),

    // 히스토리
    NOT_FOUND_HISTORY(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "해당 식당 히스토리는 존재하지 않습니다."),

    // S3
    FILE_UPLOAD_ERROR(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "잘못된 업로드 파일 형식입니다."),
    FILE_DELETE_ERROR(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "삭제하려는 파일이 존재하지 않습니다."),

    // 파라미터 누락
    PARAMETER_MISSING_ERROR(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), "필수 파라미터 값이 누락되었습니다."),

    // 식당 중복 좋아요
    DUPLICATED_FAVORITE_RESTAURANT(HttpStatus.CONFLICT, HttpStatus.CONFLICT.value(), "이미 좋아요 된 식당입니다"),
    NOT_FOUND_FAVORITE_RESTAURANT(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "좋아요에 등록되지 않아 취소할 수 없습니다."),

    // Short URL
    NOT_FOUND_SHORT_URL(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value(), "찾을 수 없는 단축 URL 입니다."),

    ;

    private final HttpStatus status;
    private final Integer statusCode;
    private final String message;

}
