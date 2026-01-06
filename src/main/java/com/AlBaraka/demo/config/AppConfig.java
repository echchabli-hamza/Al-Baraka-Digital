package com.AlBaraka.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;


@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



    @Configuration
    public class CorsConfig {

        @Bean
        public CorsFilter corsFilter() {
            CorsConfiguration config = new CorsConfiguration();

            config.setAllowCredentials(true);
            config.setAllowedOrigins(List.of(
                    "http://localhost:4200",
                    "http://161.35.220.69:4200"
            ));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", config);

            return new CorsFilter(source);
        }
    }



}
