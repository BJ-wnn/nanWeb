//package com.github.nan.web.core.config;
//
//import com.alibaba.druid.support.http.StatViewServlet;
//import com.alibaba.druid.support.http.WebStatFilter;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.boot.web.servlet.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @author NanNan Wang
// */
//@Configuration
//public class DruidConfig {
//
//    // 注册Druid的监控Servlet
//    @Bean
//    public ServletRegistrationBean<StatViewServlet> druidServlet() {
//        ServletRegistrationBean<StatViewServlet> servletRegistrationBean =
//                new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
//        // 设置登录的用户名和密码
//        servletRegistrationBean.addInitParameter("loginUsername", "admin");
//        servletRegistrationBean.addInitParameter("loginPassword", "admin123");
//        return servletRegistrationBean;
//    }
//
//    // 注册Druid的监控过滤器
//    @Bean
//    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
//        FilterRegistrationBean<WebStatFilter> filterRegistrationBean =
//                new FilterRegistrationBean<>(new WebStatFilter());
//        // 设置过滤的URL模式
//        filterRegistrationBean.addUrlPatterns("/*");
//        // 忽略的资源
//        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.css,/druid/*");
//        return filterRegistrationBean;
//    }
//}
