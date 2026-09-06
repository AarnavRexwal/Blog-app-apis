package com.codewithaarnav.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codewithaarnav.blog.entities.User;
import com.codewithaarnav.blog.repositories.UserRepo;

@Component("userSecurity")
public class UserSecurity {

    @Autowired
    private UserRepo userRepo;

    public boolean isOwner(Integer userId, String email) {

        User user = this.userRepo.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return false;
        }

        return user.getId() == userId;
    }
}