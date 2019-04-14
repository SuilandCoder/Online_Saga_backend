package com.liber.sun;

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
 * Created by sunlingzhi on 2017/10/21.
 */
@Configuration //Spring 加载该类配置
@EnableSwagger2 //启动Swagger2

//调用网址 localhost:port/swagger-ui.html#
public class Swagger2 {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.liber.sun"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RESTful APIs")
                .description("http://172.21.213.146:8000/")
                .termsOfServiceUrl("http://172.21.213.146:8000/")
                .contact("Liber Sun")
                .version("1.0")
                .build();
    }


}
