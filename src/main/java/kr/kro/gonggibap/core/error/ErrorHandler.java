package kr.kro.gonggibap.core.error;

import kr.kro.gonggibap.core.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.BindException;

import static kr.kro.gonggibap.core.error.CommonResponse.failure;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustomException(final CustomException e) {
        log.warn(e.getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(failure(e.getMessage()));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(final BindException e) {
        log.warn(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(failure(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(failure(e.getMessage()));
    }

}
