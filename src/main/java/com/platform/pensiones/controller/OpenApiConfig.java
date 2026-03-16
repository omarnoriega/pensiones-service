package com.platform.pensiones.controller;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI pensionesOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pensiones Service API")
                        .description("""
                                ## Afiliados al Sistema General de Pensiones de Colombia
                                
                                Microservicio de consulta de estadísticas de afiliación pensional.
                                
                                ### Fuente de datos
                                Archivo CSV: *Afiliados al sistema general de pensiones de Colombia*
                                
                                ### Endpoints disponibles
                                | Endpoint | Descripción |
                                |----------|-------------|
                                | `GET /api/v1/pensiones` | Todas las entidades |
                                | `GET /api/v1/pensiones/nombres` | Solo los nombres |
                                | `GET /api/v1/pensiones/buscar?nombre=X` | Buscar por nombre |
                                | `GET /api/v1/pensiones/{nombre}` | Obtener por nombre (path) |
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Platform Engineering Team")
                                .email("platform@empresa.com")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local")));
    }
}
