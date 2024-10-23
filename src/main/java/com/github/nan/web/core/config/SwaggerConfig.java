package com.github.nan.web.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * 只有本地开发和测试环境才启用swagger 生产禁止使用
 * @author NanNan Wang
 * @date 2024/3/19
 */

@Profile({"dev","test"})
@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {

    @Bean(value = "swaggerBean")
    public Docket swaggerBean() {
        //指定使用Swagger2规范
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        //描述字段支持Markdown语法
                        .description("# 项目模版dmeo")
                        .termsOfServiceUrl("https://doc.xiaominfo.com/")
                        .contact("bjcpwnn@126.com")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("demo")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.github.nan.web.demos.web"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

}
