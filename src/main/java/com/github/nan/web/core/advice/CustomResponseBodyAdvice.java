package com.github.nan.web.core.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.nan.web.core.annotation.AutoWrapResponse;
import com.github.nan.web.core.annotation.SkipResponseWrap;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.function.Predicate;


/**
 * @author NanNan Wang
 */
@ControllerAdvice
@RequiredArgsConstructor
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 判断是否需要进行封装
        boolean isAutoWrap = AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), AutoWrapResponse.class) ||
                returnType.hasMethodAnnotation(AutoWrapResponse.class);
        boolean isSkipWrap = AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), SkipResponseWrap.class) ||
                returnType.hasMethodAnnotation(SkipResponseWrap.class);

        return isAutoWrap && !isSkipWrap;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        // 返回封装后的统一格式数据
        if (body instanceof ResponseWrapper || body instanceof ResponseEntity) {
            return body;  // 已经封装的直接返回
        }

        if(body instanceof String) {
            try {
                return this.objectMapper.writeValueAsString(new ResponseWrapper<>(200, "成功", body));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }


        return new ResponseWrapper<>(200, "成功", body);  // 统一封装格式
    }



}
