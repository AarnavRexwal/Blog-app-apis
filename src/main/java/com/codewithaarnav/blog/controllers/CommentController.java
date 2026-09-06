package com.codewithaarnav.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codewithaarnav.blog.payloads.ApiResponse;
import com.codewithaarnav.blog.payloads.CommentDto;
import com.codewithaarnav.blog.services.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // POST - Create Comment
    @PostMapping("/")
    public ResponseEntity<CommentDto> createComment(
            @RequestBody CommentDto commentDto,
            Authentication authentication) {

        String email = authentication.getName();

        CommentDto createdComment =
                this.commentService.createComment(commentDto, email);

        return new ResponseEntity<>(
                createdComment,
                HttpStatus.CREATED
        );
    }

    // GET - Get Comment By ID
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getCommentById(
            @PathVariable("commentId") Integer commentId) {

        return ResponseEntity.ok(
                this.commentService.getCommentById(commentId)
        );
    }

    // GET - Get All Comments
    @GetMapping("/")
    public ResponseEntity<List<CommentDto>> getAllComments() {

        return ResponseEntity.ok(
                this.commentService.getAllComments()
        );
    }
    
    
 // PUT - Update Comment
    @PreAuthorize("hasRole('ADMIN') or @commentSecurity.isOwner(#commentId, authentication.name)")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(
            @RequestBody CommentDto commentDto,
            @PathVariable("commentId") Integer commentId) {

        CommentDto updatedComment =
                this.commentService.updateComment(commentDto, commentId);

        return ResponseEntity.ok(updatedComment);
    }

    // DELETE - Delete Comment
    @PreAuthorize("hasRole('ADMIN') or @commentSecurity.isOwner(#commentId, authentication.name)")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(
            @PathVariable("commentId") Integer commentId) {

        this.commentService.deleteComment(commentId);

        return new ResponseEntity<>(
                new ApiResponse(
                        "Comment Deleted Successfully",
                        true
                ),
                HttpStatus.OK
        );
    }
    
    
 
}