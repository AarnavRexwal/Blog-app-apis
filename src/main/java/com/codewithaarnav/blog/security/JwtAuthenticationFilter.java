package com.codewithaarnav.blog.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Get Authorization header
        String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        // Check if Authorization header exists and starts with Bearer
        if (requestTokenHeader != null
                && requestTokenHeader.startsWith("Bearer ")) {

            jwtToken = requestTokenHeader.substring(7);

            try {
                username = jwtTokenHelper.getUsernameFromToken(jwtToken);
            } catch (Exception e) {
                System.out.println("Unable to get username from JWT token");
            }

        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }

        // If username is found and user is not already authenticated
        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            System.out.println("JWT username: " + username);

            UserDetails userDetails =
                    customUserDetailsService.loadUserByUsername(username);

            System.out.println(
                    "User authorities: " + userDetails.getAuthorities()
            );

            boolean valid =
                    jwtTokenHelper.validateToken(
                            jwtToken,
                            userDetails
                    );

            System.out.println("JWT valid: " + valid);

            if (valid) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

                System.out.println("Authentication set successfully");
            }
        }

        // Continue the request
        filterChain.doFilter(request, response);
    }
}