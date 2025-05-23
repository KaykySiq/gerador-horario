package com.example.gerador_horario.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                    .info(new Info()
                        .title("API Gerador de Horario")
                        .version("1.0")
                        .description("Documentação para geração de horario."));
                            
    }
}
