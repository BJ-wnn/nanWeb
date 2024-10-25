package com.github.nan.web.core.filter;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author NanNan Wang
 */
@Data
@Accessors(chain = true)
class HttpRequestLog {

    private LocalDateTime timestamp;
    private String traceId;
    private String method;
    private String uri;
    private String queryString;
    private String remoteAddr;
    private String args;      // 请求参数
    private String payload;     // json请求字符串
    private int responseCode;    // 响应码
    private Object result;      // 请求结果
    private long duration;      // 执行时间

}
