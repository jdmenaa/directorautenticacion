package com.technisys.director.services.directorAutenticacion.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Swagger configuration
 */
@Configuration
public class SwaggerApiConfiguration {  
	
    @Value("${springdoc.server.url}")
    private String urlServer;

    @Value("${springdoc.server.description}")
    private String descriptionlServer;
	
    @Bean
    public OpenAPI directorOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("directorAutenticacion")
                .description("Put here the description of this director")
                .version("1.0.0"))
                .addServersItem(new Server().url(urlServer).description(descriptionlServer));
    }
	
}
