package com.codewithaarnav.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.codewithaarnav.blog.security.JwtAuthenticationEntryPoint;
import com.codewithaarnav.blog.security.JwtAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;



import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.Customizer;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationEntryPoint point;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
            // Disable CSRF because this is a stateless REST API
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())

            // Configure unauthorized request handling
            .exceptionHandling(exception -> exception
            	    .authenticationEntryPoint(point)
            	    .accessDeniedHandler((request, response, accessDeniedException) -> {
            	        System.out.println("ACCESS DENIED: "
            	                + accessDeniedException.getMessage());

            	        response.sendError(
            	                HttpServletResponse.SC_FORBIDDEN,
            	                "Forbidden"
            	        );
            	    })
            	)

            // Do not create HTTP sessions
            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            // Configure URL authorization
            .authorizeHttpRequests(auth -> auth

            	    // Login and public resources
            	    .requestMatchers(
            	        "/api/auth/login",
            	        "/api/users/",
            	        "/error",
            	        "/swagger-ui/**",
            	        "/swagger-ui.html",
            	        "/v3/api-docs/**",
            	        "/api/post/image/**"
            	    ).permitAll()

            	    // Everything else requires authentication
            	    .anyRequest().authenticated()

            	);

        // Add JWT filter before Spring Security's username/password filter
        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }
    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
            new CorsConfiguration();

        configuration.setAllowedOrigins(
            java.util.List.of(
                "http://localhost:5173"
            )
        );

        configuration.setAllowedMethods(
            java.util.List.of(
                "GET",
                "POST",
                "PUT",
                "DELETE",
                "PATCH",
                "OPTIONS"
            )
        );

        configuration.setAllowedHeaders(
            java.util.List.of(
                "Authorization",
                "Content-Type",
                "Accept",
                "Origin",
                "X-Requested-With"
            )
        );

        configuration.setExposedHeaders(
            java.util.List.of(
                "Authorization"
            )
        );

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
            new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
            "/**",
            configuration
        );

        return source;
    }
    

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration builder)
            throws Exception {

        return builder.getAuthenticationManager();
    }
}