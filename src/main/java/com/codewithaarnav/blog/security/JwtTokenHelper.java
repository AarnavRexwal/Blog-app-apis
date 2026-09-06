package com.codewithaarnav.blog.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenHelper {

    @Value("${jwt.secret}")
    private String secret;

    // Token validity: 1 hour
    private final long JWT_TOKEN_VALIDITY = 60 * 60 * 1000;

    // Generate signing key
    private SecretKey getSigningKey() {

        byte[] keyBytes = Decoders.BASE64.decode(secret);

        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate JWT token
    public String generateToken(UserDetails userDetails) {

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis()
                                + JWT_TOKEN_VALIDITY)
                )
                .signWith(getSigningKey())
                .compact();
    }

    // Get username/email from JWT
    public String getUsernameFromToken(String token) {

        Claims claims = getClaimsFromToken(token);

        return claims.getSubject();
    }

    // Get all claims from JWT
    private Claims getClaimsFromToken(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Check whether token is expired
    public boolean isTokenExpired(String token) {

        Date expiration = getExpirationDateFromToken(token);

        return expiration.before(new Date());
    }

    // Get expiration date from JWT
    private Date getExpirationDateFromToken(String token) {

        Claims claims = getClaimsFromToken(token);

        return claims.getExpiration();
    }

    // Validate JWT token
    public boolean validateToken(
            String token,
            UserDetails userDetails) {

        try {

            String username = getUsernameFromToken(token);

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);

        } catch (Exception e) {

            return false;
        }
    }
}