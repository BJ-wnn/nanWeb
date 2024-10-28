package com.github.nan.web.core.advice;

import com.github.nan.web.core.constant.HttpStatusEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author NanNan Wang
 */
@RestControllerAdvice(basePackages="com.github.nan.web")
public class CustomExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseWrapper handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        BindingResult bindingResult =ex.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder("校验失败:");
        for ( FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":").append(fieldError.getDefaultMessage()).append(";");
        }
        return ResponseWrapper.fail(HttpStatusEnum.ILLEGAL_ARGUMENTS_MIXED, stringBuilder.toString());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleException(Exception ex) {
        // 记录异常日志
        ex.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
