package com.gocle.lxp.tracking.config;

import com.gocle.lxp.tracking.security.ApiKeyFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final ApiKeyFilter apiKeyFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Tracking API는 세션/CSRF 불필요
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // 실제 인증은 ApiKeyFilter에서 처리
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            )
            // ⭐ API Key Filter 등록 (JWT 없음)
            .addFilterBefore(
                apiKeyFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}
