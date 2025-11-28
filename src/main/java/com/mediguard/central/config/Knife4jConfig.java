package com.mediguard.central.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Knife4j / OpenAPI 配置类
 * 通过 knife4j.enable 属性控制是否开启
 */
@Configuration
@ConditionalOnProperty(name = "knife4j.enable", havingValue = "true", matchIfMissing = true)
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MediGuard Central Station API")
                        .version("1.0")
                        .description("MediGuard 中央监护站后端接口文档")
                        .contact(new Contact()
                                .name("MediGuard Team")
                                .email("support@mediguard.com")));
    }
}
