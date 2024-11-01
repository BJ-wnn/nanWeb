package com.github.nan.web.core.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.mapping.BoundSql;

import java.sql.Statement;

/**
 * @author NanNan Wang
 */


@Intercepts({
        @Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}),
        @Signature(type = StatementHandler.class, method = "update", args = {Statement.class}),
        @Signature(type = StatementHandler.class, method = "batch", args = {Statement.class})
})
@Slf4j
public class SqlExecutionInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取 SQL 语句
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql().replaceAll("\\s+", " ").trim();

        Object parameterObject = statementHandler.getBoundSql().getParameterObject();

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 执行 SQL
        Object result = invocation.proceed();

        // 计算执行时间
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // 记录 SQL 语句和执行时间
        log.info("SQL: " + sql);
        log.info("Parameters: " + parameterObject);
        log.info("Execution time: " + executionTime + " ms");

        return result;
    }

}
