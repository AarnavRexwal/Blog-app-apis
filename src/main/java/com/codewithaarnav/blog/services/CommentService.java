package com.codewithaarnav.blog.services;

import java.util.List;

import com.codewithaarnav.blog.payloads.CommentDto;

public interface CommentService {

    // CREATE COMMENT
    CommentDto createComment(CommentDto commentDto, String email);

    // GET COMMENT BY ID
    CommentDto getCommentById(Integer commentId);

    // GET ALL COMMENTS
    List<CommentDto> getAllComments();

    // UPDATE COMMENT
    CommentDto updateComment(CommentDto commentDto, Integer commentId);

    // DELETE COMMENT
    void deleteComment(Integer commentId);
}