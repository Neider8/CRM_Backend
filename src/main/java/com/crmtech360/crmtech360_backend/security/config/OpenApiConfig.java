package com.crmtech360.crmtech360_backend.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "TelasTech360 API",
                version = "v1.0", // Puedes ajustar la versión
                description = "API para el sistema de gestión de confección textil TelasTech360."
                // Puedes añadir más información como contacto, licencia, etc.
                // contact = @Contact(name = "Tu Nombre/Empresa", email = "tuemail@example.com", url = "https://tuweb.com"),
                // license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        )
)
@SecurityScheme(
        name = "bearerAuth", // Este es el nombre que usas en @SecurityRequirement
        description = "Autenticación JWT usando el esquema Bearer. Ingresa el token JWT precedido por 'Bearer '. Ejemplo: 'Bearer {token}'",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // No se necesitan más métodos o beans aquí si solo es para esta definición.
    // Spring Boot y springdoc-openapi se encargarán del resto.
}