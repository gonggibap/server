package kr.kro.gonggibap.core.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ValidationError {
    private String field;
    private String code;
    private String message;
    private String objectName;
}
