package com.lab2.middleware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        // Use allowedOriginPatterns instead of allowedOrigins when allowCredentials is true
        config.addAllowedOriginPattern("*");
        
        // Allow credentials
        config.setAllowCredentials(true);
        
        // Allow all headers
        config.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Cache-Control",
            "Content-Type",
            "Accept",
            "Origin",
            "X-Requested-With",
            "Access-Control-Request-Method",
            "Access-Control-Request-Headers"
        ));
        
        // Allow all methods including OPTIONS for preflight
        config.setAllowedMethods(Arrays.asList(
            "GET", "POST", "PUT", "DELETE", "PATCH", "HEAD", "OPTIONS"
        ));
        
        // Set max age for preflight requests
        config.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}