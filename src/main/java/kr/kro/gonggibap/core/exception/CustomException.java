package kr.kro.gonggibap.core.exception;

import kr.kro.gonggibap.core.error.ErrorCode;
import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;
    private Errors errors;


    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public CustomException(ErrorCode errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public CustomException(ErrorCode errorCode, Errors errors) {
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
        this.errors = errors;
    }

    public CustomException(ErrorCode errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public CustomException(ErrorCode errorCode, String message, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
        this.message = message;
    }
}
