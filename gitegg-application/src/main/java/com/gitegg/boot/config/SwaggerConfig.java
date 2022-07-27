package com.gitegg.boot.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author GitEgg
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {

    @Bean(value = "gitegg-oauth-api")
    public Docket GitEgOauthApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("1、GitEgg认证接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gitegg.boot.oauth"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "gitegg-system-api")
    public Docket GitEggSystemApi() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //分组名称
                .groupName("2、GitEgg系统接口")
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.gitegg.boot.system"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    @Bean(value = "gitegg-extension-api")
    public Docket GitEggExtensionApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("3、GitEgg扩展接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gitegg.boot.extension"))
                .paths(PathSelectors.any())
                .build();
    }

    @Bean(value = "gitegg-generator-api")
    public Docket GitEgGeneratorApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("4、GitEgg代码生成接口")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gitegg.boot.generator"))
                .paths(PathSelectors.any())
                .build();
    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().version("1.0.3")
                .title("GitEgg Boot 文档")
                .description("GitEgg Boot 文档")
                .termsOfServiceUrl("www.boot.gitegg.com")
                .build();
    }

}
