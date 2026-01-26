package it.unicam.cs.diciotto.progettoids.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI progettoIdsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Progetto IDS - Filiera Agricola API")
                        .version("1.0.0")
                        .description("Documentazione API per il progetto Filiera Agricola"));
    }
}
