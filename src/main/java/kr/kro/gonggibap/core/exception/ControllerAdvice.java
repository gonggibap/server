package kr.kro.gonggibap.core.exception;

import kr.kro.gonggibap.core.error.ErrorCode;
import kr.kro.gonggibap.core.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@RequiredArgsConstructor
public class ControllerAdvice {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        ErrorResponse errorResponse = null;
        if (e.getErrors() != null) {
            errorResponse = ErrorResponse.of(e, e.getErrors());
        } else {
            errorResponse = ErrorResponse.of(e);
        }

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        if(!(e instanceof CustomException)) {
            log.error("서버 에러입니다.", e);
        }
        return ResponseEntity.status(500)
                .body(ErrorResponse.of(new CustomException(ErrorCode.INTERNAL_SERVER_ERROR)));
    }
}
