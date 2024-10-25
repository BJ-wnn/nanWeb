//package com.github.nan.web.core.advice;
//
//import com.github.nan.web.core.annotation.IgnoreRequestLogging;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.slf4j.MDC;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.function.Consumer;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @author NanNan Wang
// */
//@Aspect
//@Component
//public class WebRequestLoggingAspect {
//
//    // 获取名为 "reqLog" 的日志记录器
//    private static final Logger logger = LoggerFactory.getLogger("reqLog");
//
//
//    @Around("execution(* com.github.nan.web.*.controller..*(..))")
//    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//
//
//        long startTime = System.currentTimeMillis();
//        Object result = joinPoint.proceed();
//        long duration = System.currentTimeMillis() - startTime;
//
//        Consumer<Object> reqLog = obj -> {
//            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//            HttpRequestLog log = new HttpRequestLog();
//            log.setTraceId(MDC.get("traceId"));
//            log.setMethod(request.getMethod());
//            log.setUri(request.getRequestURI());
//            log.setQueryString(request.getQueryString());
//
//System.out.println(joinPoint.getArgs().toString());
//            log.setArgs(joinPoint.getArgs());
//            log.setRemoteAddr(request.getRemoteAddr());
//
//            log.setArgs(joinPoint.getArgs());
//            IgnoreRequestLogging ignoreLogging = getIgnoreRequestLogging(joinPoint);
//            if (ignoreLogging == null || ignoreLogging.saveParam()) {
//
//            }
//
//            if (ignoreLogging == null || ignoreLogging.saveResp()) {
//                log.setResult(result);
//            }
//            log.setDuration(duration);
//
//            logger.info("Request completed: {}", log);
//        };
//
//        reqLog.accept(result);
//
//
//        return result;
//    }
//
//    // 获取注解信息，如果类或方法上存在 IgnoreRequestLogging 注解则返回
//    private IgnoreRequestLogging getIgnoreRequestLogging(JoinPoint joinPoint) {
//        Class<?> targetClass = joinPoint.getTarget().getClass();
//        IgnoreRequestLogging classAnnotation = targetClass.getAnnotation(IgnoreRequestLogging.class);
//        if (classAnnotation != null) {
//            return classAnnotation;
//        }
//        // 检查方法上的注解
//        try {
//            return targetClass.getMethod(joinPoint.getSignature().getName(), ((ProceedingJoinPoint)joinPoint).getArgs().getClass())
//                    .getAnnotation(IgnoreRequestLogging.class);
//        } catch (NoSuchMethodException e) {
//            return null;
//        }
//    }
//
//}
