package com.grctool.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class WebConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 1. Explicitly allow Next.js
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        // 2. REQUIRED for JSESSIONID cookies
        config.setAllowCredentials(true);
        
        // 3. Allow all common headers and methods
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization", "X-Requested-With"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // 4. How long the browser should cache this (Senior Tip: 30 mins)
        config.setMaxAge(1800L);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}