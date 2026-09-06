package com.codewithaarnav.blog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithaarnav.blog.entities.Comment;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

}