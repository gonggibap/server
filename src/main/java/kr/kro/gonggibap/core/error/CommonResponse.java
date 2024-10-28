package kr.kro.gonggibap.core.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import kr.kro.gonggibap.core.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private boolean success;
    private T data;
    private String error;
    private int errorCode;

    public CommonResponse(final boolean success, final T data) {
        this.success = success;
        this.data = data;
    }

    public CommonResponse(final boolean success, final String error, final int errorCode) {
        this.success = success;
        this.error = error;
        this.errorCode = errorCode;
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, data);
    }

    public static <T> CommonResponse<T> failure(CustomException error) {
        return new CommonResponse<>(false, error.getMessage(), error.getErrorCode().getStatusCode());
    }

    public static <T> CommonResponse<T> customFailure(String error, int errorCode) {
        return new CommonResponse<>(false, error, errorCode);
    }
}
