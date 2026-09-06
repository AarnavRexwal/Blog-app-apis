package com.codewithaarnav.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codewithaarnav.blog.entities.Post;
import com.codewithaarnav.blog.entities.User;
import com.codewithaarnav.blog.repositories.PostRepo;
import com.codewithaarnav.blog.repositories.UserRepo;

@Component("postSecurity")
public class PostSecurity {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    public boolean isOwner(Integer postId, String email) {

        Post post = this.postRepo.findById(postId)
                .orElse(null);

        if (post == null) {
            return false;
        }

        User user = this.userRepo.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return false;
        }

        return post.getUser().getId() == user.getId();
    }

    public boolean isUser(Integer userId, String email) {

        User user = this.userRepo.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return false;
        }

        return user.getId() == userId;
    }
}