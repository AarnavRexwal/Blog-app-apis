package com.codewithaarnav.blog.payloads;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

    private String token;

    private String username;

    public LoginResponse(String token, String username) {
        this.token = token;
        this.username = username;
    }
}