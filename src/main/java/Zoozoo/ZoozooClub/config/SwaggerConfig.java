package Zoozoo.ZoozooClub.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class SwaggerConfig {

    public static final String JWT = "JWT";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo())
                .components(new Components().addSecuritySchemes(JWT, new SecurityScheme()
                        .name(JWT)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("Bearer")
                        .bearerFormat(JWT)
                        .in(SecurityScheme.In.HEADER)
                        .name(HttpHeaders.AUTHORIZATION)));
    }

    private Info apiInfo() {
        return new Info()
                .title("Zoozoo Club Swagger UI")
                .description("Zoozoo Club의 API 명세서 입니다.")
                .version("1.0.0");
    }
}
