package com.codewithaarnav.blog.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.codewithaarnav.blog.entities.Comment;
import com.codewithaarnav.blog.entities.User;
import com.codewithaarnav.blog.repositories.CommentRepo;
import com.codewithaarnav.blog.repositories.UserRepo;

@Component("commentSecurity")
public class CommentSecurity {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;

    public boolean isOwner(Integer commentId, String email) {

        Comment comment = this.commentRepo.findById(commentId)
                .orElse(null);

        if (comment == null) {
            return false;
        }

        User user = this.userRepo.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return false;
        }

        return comment.getUser().getId() == user.getId();
    }
}