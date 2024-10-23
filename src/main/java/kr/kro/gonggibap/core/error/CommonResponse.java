package kr.kro.gonggibap.core.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private boolean success;
    private Integer status;
    private T data;
    private String error;

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, null, data, null);
    }

    public static <T> CommonResponse<T> failure(String error, int statusCode) {
        return new CommonResponse<>(false, statusCode, null, error);
    }
}
