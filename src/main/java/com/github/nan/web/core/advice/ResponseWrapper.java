package com.github.nan.web.core.advice;

import com.github.nan.web.core.constant.HttpStatusEnum;
import lombok.Data;

/**
 * @author NanNan Wang
 */
@Data
class ResponseWrapper<T> {

    private int code;
    private String message;
    private T data;

    public ResponseWrapper(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseWrapper<T> fail(HttpStatusEnum status, Object... args) {
        return new ResponseWrapper<>(status.getCode(), String.format(status.getMsg(), args), null);
    }

    public static <T> ResponseWrapper<T> fail(HttpStatusEnum status) {
        return new ResponseWrapper<>(status.getCode(), status.getMsg(), null);
    }


}
