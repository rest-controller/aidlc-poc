package com.example.authapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Only the configured frontend origin may call POST /api/login and /api/signup from a browser. */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String allowedOrigin;

    public CorsConfig(@Value("${app.cors.allowed-origin:http://localhost:5173}") String allowedOrigin) {
        this.allowedOrigin = allowedOrigin;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/login")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("POST")
                .allowedHeaders("Content-Type")
                .allowCredentials(false);
        // Same single origin for sign-up (BR1.8).
        registry.addMapping("/api/signup")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("POST")
                .allowedHeaders("Content-Type")
                .allowCredentials(false);
    }
}
