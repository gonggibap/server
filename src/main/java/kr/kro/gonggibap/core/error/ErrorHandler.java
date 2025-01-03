package kr.kro.gonggibap.core.error;

import kr.kro.gonggibap.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.BindException;

import static kr.kro.gonggibap.core.error.CommonResponse.failure;
import static kr.kro.gonggibap.core.error.ErrorCode.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(final CustomException e) {
        log.warn(e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(failure(e));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(final BindException e) {
        log.warn(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(failure(INVALID_INPUT_VALUE.getMessage(), INVALID_INPUT_VALUE.getStatusCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(failure(INVALID_INPUT_VALUE.getMessage(), INVALID_INPUT_VALUE.getStatusCode()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParams(MissingServletRequestParameterException e) {
        String paramName = e.getParameterName();
        log.warn("필수 파라미터 누락: {}", paramName);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(failure(PARAMETER_MISSING_ERROR.getMessage(), PARAMETER_MISSING_ERROR.getStatusCode()));
    }

    // 파일 크기 초과 에러
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<?> handleMaxSizeException(MaxUploadSizeExceededException e) {
        log.warn(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(failure(FILE_SIZE_EXCEED.getMessage(), FILE_SIZE_EXCEED.getStatusCode()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleBusinessException(final RuntimeException e) {
        log.warn(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(failure(INTERNAL_SERVER_ERROR.getMessage(), INTERNAL_SERVER_ERROR.getStatusCode()));
    }

}
