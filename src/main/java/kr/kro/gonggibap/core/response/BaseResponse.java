package kr.kro.gonggibap.core.response;

import lombok.Getter;

@Getter
public abstract class BaseResponse<T> {
    protected boolean success;
    protected int status;
    protected String message;
    protected T data;
    protected Object errors;
}
