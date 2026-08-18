package com.cogitolab.notificationhub.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("NotificationHub API — Cogito Lab")
                .version("1.0.0")
                .description("Central de Ingestão de Eventos IoT, Motor de Regras e Notificações Assíncronas para a Fazenda Boa Esperança.")
                .contact(new Contact()
                    .name("Cogito Lab Challenge Team")
                    .email("support@cogitolab.com")));
    }
}
