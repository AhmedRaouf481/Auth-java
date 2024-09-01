package com.araouf.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final String[] WHITE_LIST_URL = {
                        "/api/v1/auth",
                        "/api/v1/auth/login",
                        "/error"
        };

        private final AuthenticationProvider authenticationProvider;
        private final JwtAuthenticationFilter jwtAuthFilter;

        @Autowired
        @Qualifier("customAuthenticationEntryPoint")
        AuthenticationEntryPoint authEntryPoint;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                        .csrf((csrf) -> csrf.disable())
                        .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL)
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                        .sessionManagement((session) -> session
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(authenticationProvider)
                        .httpBasic(basic -> basic.authenticationEntryPoint(authEntryPoint))
                        .exceptionHandling(Customizer.withDefaults())
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }
}
