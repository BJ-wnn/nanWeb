package com.github.nan.web.core.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * @author NanNan Wang
 */

@Component
@Order(2)
@RequiredArgsConstructor
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger("reqLog");


    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        // 检查是否需要跳过日志记录
        if (shouldSkipLogging(request)) {
            try{
                filterChain.doFilter(request, response);
            } finally {
                logRequest(request);
            }
            return;
        }

        HttpServletRequest wrappedRequest = wrapRequest(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        try {
            // 继续处理请求
            filterChain.doFilter(wrappedRequest,wrappedResponse);
        } finally {
            // 请求结束后记录日志
            long duration = System.currentTimeMillis() - startTime;
            logRequest(wrappedRequest,wrappedResponse,duration);
            wrappedResponse.copyBodyToResponse();  // 将缓存的响应内容写回实际响应对象
        }
    }

    private HttpServletRequest wrapRequest(HttpServletRequest request) {
        // 仅在 POST 请求的 JSON 请求体时进行包装，确保缓存请求体
        if (HttpMethod.POST.name().equalsIgnoreCase(request.getMethod()) && MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())) {
            return new ContentCachingRequestWrapper(request);
        }
        return request;
    }

    private void logRequest(HttpServletRequest request, ContentCachingResponseWrapper response, long duration) throws JsonProcessingException {

        Function<HttpServletRequest,HttpRequestLog> saveRequest = req -> {
            HttpRequestLog requestLog = new HttpRequestLog()
                    .setTraceId(MDC.get("traceId"))
                    .setRemoteAddr(request.getRemoteAddr())
                    .setUri(request.getRequestURI())
                    .setMethod(request.getMethod())
                    .setQueryString(request.getQueryString())
                    .setTimestamp(LocalDateTime.now());
            if(req instanceof ContentCachingRequestWrapper) {
                ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) req;
                byte[] content = wrapper.getContentAsByteArray();
                try {
                    requestLog.setPayload(new String(content, wrapper.getCharacterEncoding()));
                } catch (UnsupportedEncodingException e) {
                    requestLog.setPayload("[unknown encoding]");
                }
            } else {
                String paramsAsString = req.getParameterMap().entrySet().stream()
                        .map(entry -> {
                            String key = entry.getKey();
                            String[] values = entry.getValue();
                            return key + "=" + String.join(",", values); // 用逗号连接多个值
                        })
                        .collect(Collectors.joining("&")); // 使用 `&` 连接多个参数键值对
                requestLog.setArgs(paramsAsString);
            }
            return requestLog;
        };

        Function<HttpRequestLog,HttpRequestLog> saveResponse = reqLog -> {
            byte[] responseContent = response.getContentAsByteArray();
            try {
                String responseBody = new String(responseContent, response.getCharacterEncoding());
                reqLog.setResult(responseBody);  // 记录响应内容
            } catch (UnsupportedEncodingException e) {
                reqLog.setResult("[unknown encoding]");
            }
            reqLog.setResponseCode(response.getStatus());
            return reqLog;
        };


        logger.info(objectMapper.writeValueAsString(saveRequest.andThen(saveResponse).apply(request)));
    }

    private void logRequest(HttpServletRequest request) throws JsonProcessingException {
        HttpRequestLog requestLog = new HttpRequestLog()
                .setTraceId(MDC.get("traceId"))
                .setRemoteAddr(request.getRemoteAddr())
                .setUri(request.getRequestURI())
                .setMethod(request.getMethod())
                .setQueryString(request.getQueryString())
                .setTimestamp(LocalDateTime.now());;
        logger.info(objectMapper.writeValueAsString(requestLog));
    }


    private boolean shouldSkipLogging(HttpServletRequest request) {
        // 根据URI路径或Content-Type检查是否为文件上传/下载
        String uri = request.getRequestURI();
        String contentType = request.getContentType();

        // 例如，跳过包含 "/upload" 或 "/download" 的请求，或 Content-Type 为 multipart/form-data 的请求
        return uri.contains("/upload") || uri.contains("/download") ||
                (contentType != null && contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) ||
                (contentType != null && contentType.equals(MediaType.APPLICATION_OCTET_STREAM_VALUE));
    }

}
