package kr.kro.gonggibap.core.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nimbusds.jose.shaded.gson.Gson;
import kr.kro.gonggibap.core.exception.CustomException;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.SimpleErrors;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponse extends BaseResponse<Void> {
    private ErrorResponse(boolean success, int code, String message, Errors errors) {
        this.success = success;
        this.status = code;
        this.message = message;
        this.errors = parseErrors(errors);
    }

    private ErrorResponse(CustomException exception) {
        this(false, exception.getErrorCode().getStatus().value(), exception.getMessage(), new SimpleErrors("error", exception.getMessage()));
    }

    private ErrorResponse(CustomException exception, String message) {
        this(false, exception.getErrorCode().getStatus().value(), message, null);
    }

    public static ErrorResponse of(CustomException exception) { return new ErrorResponse(exception); }

    public static ErrorResponse of(CustomException exception, String message) {
        return new ErrorResponse(exception, message);
    }

    public static ErrorResponse of(Exception exception) {
        return new ErrorResponse(false, 500, exception.getMessage(), null);
    }

    public static ErrorResponse of(CustomException exception, Errors errors) {
        return new ErrorResponse(false, exception.getErrorCode().getStatus().value(), exception.getMessage(), errors);
    }

    private List<ValidationError> parseErrors(Errors errors) {
        if(errors == null) return new ArrayList<>();

        List<ValidationError> customErrors = new ArrayList<>();
        for (FieldError error : errors.getFieldErrors()) {
            customErrors.add(new ValidationError(error.getField(), error.getCode(), error.getDefaultMessage(), error.getObjectName()));
        }
        for (ObjectError error : errors.getGlobalErrors()) {
            customErrors.add(new ValidationError(null, error.getCode(), error.getDefaultMessage(), error.getObjectName()));
        }
        return customErrors;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @JsonIgnore
    @Override
    public Void getData() {
        return super.getData();
    }




}
