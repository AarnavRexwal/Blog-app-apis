package com.codewithaarnav.blog.security;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder =
                new BCryptPasswordEncoder();

        // USER password
        String userPassword = "123456";
        String userHash = encoder.encode(userPassword);

        // ADMIN password
        String adminPassword = "admin123";
        String adminHash = encoder.encode(adminPassword);

        System.out.println("USER password: " + userPassword);
        System.out.println("USER BCrypt hash: " + userHash);

        System.out.println();

        System.out.println("ADMIN password: " + adminPassword);
        System.out.println("ADMIN BCrypt hash: " + adminHash);
    }
}