package com.example.test_swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author 邵祺锴
 * @create 2020-01-15 14:15
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                // 接口包扫描，也可配置全项目扫描
                .apis(RequestHandlerSelectors.basePackage("com.example.test_swagger"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    // API自定义描述信息
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Qikai Shao Project APITest")
                .description("RestFul接口 API")
                .termsOfServiceUrl("http://localhost:8081/kai")
                .version("1.0.0").build();
    }

}
