package com.codewithaarnav.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewithaarnav.blog.entities.Comment;
import com.codewithaarnav.blog.entities.Post;
import com.codewithaarnav.blog.entities.User;
import com.codewithaarnav.blog.exceptions.ResourceNotFoundException;
import com.codewithaarnav.blog.payloads.CommentDto;
import com.codewithaarnav.blog.repositories.CommentRepo;
import com.codewithaarnav.blog.repositories.PostRepo;
import com.codewithaarnav.blog.repositories.UserRepo;
import com.codewithaarnav.blog.services.CommentService;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    // CREATE COMMENT
    @Override
    public CommentDto createComment(CommentDto commentDto, String email) {

        // Get the logged-in user from JWT email
        User user = this.userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        "User not found with email: " + email));

        // Get the post using postId
        Post post = this.postRepo.findById(commentDto.getPostId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Post",
                        "id",
                        commentDto.getPostId()));

        // Create comment
        Comment comment = new Comment();

        comment.setContent(commentDto.getContent());

        // Set logged-in user as comment owner
        comment.setUser(user);

        // Set post for the comment
        comment.setPost(post);

        // Save comment
        Comment savedComment = this.commentRepo.save(comment);

        // Convert entity to DTO
        return this.commentToDto(savedComment);
    }

    // GET COMMENT BY ID
    @Override
    public CommentDto getCommentById(Integer commentId) {

        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comment",
                        "id",
                        commentId));

        return this.commentToDto(comment);
    }

    // GET ALL COMMENTS
    @Override
    public List<CommentDto> getAllComments() {

        List<Comment> comments = this.commentRepo.findAll();

        return comments.stream()
                .map(comment -> this.commentToDto(comment))
                .collect(Collectors.toList());
    }
    
 // UPDATE COMMENT
    @Override
    public CommentDto updateComment(CommentDto commentDto, Integer commentId) {

        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comment",
                        "id",
                        commentId));

        comment.setContent(commentDto.getContent());

        Comment updatedComment = this.commentRepo.save(comment);

        return this.commentToDto(updatedComment);
    }

    // DELETE COMMENT
    @Override
    public void deleteComment(Integer commentId) {

        Comment comment = this.commentRepo.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Comment",
                        "id",
                        commentId));

        this.commentRepo.delete(comment);
    }

    // CONVERT COMMENT TO COMMENT DTO
    private CommentDto commentToDto(Comment comment) {

        CommentDto commentDto = new CommentDto();

        commentDto.setCommentId(comment.getCommentId());
        commentDto.setContent(comment.getContent());
        commentDto.setUserId(comment.getUser().getId());
        commentDto.setPostId(comment.getPost().getPostId());

        return commentDto;
    }
    
    
 
}