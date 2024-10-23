package kr.kro.gonggibap.core.error;

import kr.kro.gonggibap.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static kr.kro.gonggibap.core.error.CommonResponse.failure;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleStockNotFoundException(final CustomException e) {
        log.warn(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(failure(e.getMessage(), e.getErrorCode().getStatusCode()));
    }
}
