package com.ufps.animeapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Aplica a todas las rutas
                        .allowedOrigins("*") // Cambia esto por el origen de tu frontend
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH") // Métodos permitidos
                        .allowedHeaders("*"); // Permite todos los headers
            }
        };
    }
}
