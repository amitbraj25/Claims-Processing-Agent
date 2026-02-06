package com.synapx.claims.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI claimsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Autonomous Insurance Claims Processing Agent API")
                        .description("""
                                This API processes FNOL (First Notice of Loss) documents in PDF/TXT format.
                                
                                Features:
                                - Extracts key policy, incident, asset, and party fields
                                - Detects missing mandatory fields
                                - Applies routing rules to recommend claim workflow
                                
                                Routing Rules:
                                - Estimated damage < 25000 -> Fast-track
                                - Missing mandatory fields -> Manual review
                                - Description contains fraud/inconsistent/staged -> Investigation Flag
                                - Claim type = injury -> Specialist Queue
                                """)
                        .version("1.0.0"));
    }
}
