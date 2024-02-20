package com.reddit.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfiguration {
    @Bean
    public OpenAPI getApiInfo(){
        return new OpenAPI()
                .info(new Info().title("Reddit Clone API")
                                .version("API for Reddit clone application")
                                .license(new License().name("Apache Licence version whatever")))
                .externalDocs(new ExternalDocumentation().description("Text something")
                                                         .url("https://expensetracker.wiki/docs"));


    }
}
