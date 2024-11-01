package com.github.nan.web.core.config;

import com.github.nan.web.core.interceptor.SqlExecutionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author NanNan Wang
 */
@Configuration
public class MyBatisConfig {

    @Bean
    public SqlExecutionInterceptor sqlExecutionInterceptor() {
        return new SqlExecutionInterceptor();
    }
}
