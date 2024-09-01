package com.treadingPlatformApplication.config;
import com.treadingPlatformApplication.config.jwt.JwtTokenValidation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@Configuration
public class WebSecurityService {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.sessionManagement(management->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth->auth.requestMatchers("/api/**").authenticated().anyRequest().permitAll())
                .addFilterBefore(new JwtTokenValidation(), BasicAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors->cors.configurationSource(coreConfiguration()));
        return httpSecurity.build();
    }

    private CorsConfigurationSource coreConfiguration() {
        return new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                CorsConfiguration cf = new CorsConfiguration();
                cf.setAllowedOrigins(
                        Arrays.asList("http://localhost:5173","http://localhost:3000","https://main--treading.netlify.app/")
                );
                cf.setAllowedMethods(Collections.singletonList("*"));
                cf.setAllowCredentials(true);
                cf.setExposedHeaders(Arrays.asList("Authorization"));
                cf.setAllowedHeaders(Collections.singletonList("*"));
                cf.setMaxAge(3600L);
                return cf;
            }
        };
    }

}
