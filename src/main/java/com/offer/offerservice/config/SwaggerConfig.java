package com.offer.offerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket produceApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metaInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.offer.offerservice"))
                .paths(regex("/.*"))
                .build();
    };
    private ApiInfo metaInfo(){
        ApiInfo apiInfo = new ApiInfo("Office Service Application Demonstration","Offer Service","1.0","Terms.URL.",new Contact("Biju Pillai","http://dummy.html","sniju@gmail.com"),"Apache","http://apachelicence.com");
            return apiInfo;
    }
}
