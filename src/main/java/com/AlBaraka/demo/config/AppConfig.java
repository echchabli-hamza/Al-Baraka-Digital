package com.AlBaraka.demo.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashSet;
import java.util.Set;
import java.util.List;


@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }




    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService() {
        return userRequest -> {
            OAuth2User oauth2User = new DefaultOAuth2UserService().loadUser(userRequest);

            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

            mappedAuthorities.addAll(oauth2User.getAuthorities());

            System.out.println("Authorities reçues du token :");
            mappedAuthorities.forEach(System.out::println);

            String scopeClaim = oauth2User.getAttribute("scp");
            if (scopeClaim != null) {
                for (String scope : scopeClaim.split(" ")) {
                    mappedAuthorities.add(new SimpleGrantedAuthority("SCOPE_" + scope));
                }
            }

            return new DefaultOAuth2User(mappedAuthorities, oauth2User.getAttributes(), "sub");
        };
    }


//    @Configuration
//    public class CorsConfig {
//
//        @Bean
//        public CorsFilter corsFilter() {
//            CorsConfiguration config = new CorsConfiguration();
//
//            config.setAllowCredentials(true);
//            config.setAllowedOrigins(List.of(
//                    "http://localhost:4200",
//                    "http://161.35.220.69:4200"
//            ));
//            config.setAllowedHeaders(List.of("*"));
//            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//
//            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//            source.registerCorsConfiguration("/**", config);
//
//            return new CorsFilter(source);
//        }
//    }



}
