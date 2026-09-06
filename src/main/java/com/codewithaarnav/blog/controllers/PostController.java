package com.codewithaarnav.blog.controllers;

import org.springframework.http.MediaType;
import java.io.InputStream;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.codewithaarnav.blog.config.AppConstants;
import com.codewithaarnav.blog.payloads.PostDto;
import com.codewithaarnav.blog.services.FileService;
import com.codewithaarnav.blog.services.PostService;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;
    
    @Value("${project.image}")
    private String path;

    @Autowired
    private FileService fileService;

    // CREATE POST
    @PostMapping("/Users/{userId}/category/{categoryId}/posts")
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isUser(#userId, authentication.name)")
    public ResponseEntity<PostDto> createPost(
            @Valid @RequestBody PostDto postDto,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId) {

        PostDto createdPost = this.postService.createPost(
                postDto,
                userId,
                categoryId
        );

        return new ResponseEntity<>(
                createdPost,
                HttpStatus.CREATED
        );
    }

   
   // GET ALL POSTS WITH PAGINATION AND SORTING

    @GetMapping("/posts/page")
    public ResponseEntity<Page<PostDto>> getAllPostsWithPaginationAndSorting(
            @RequestParam(defaultValue = AppConstants.PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = AppConstants.PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConstants.SORT_BY) String sortBy,
            @RequestParam(defaultValue = AppConstants.SORT_DIR) String sortDir) {

        Page<PostDto> posts =
                this.postService.getAllPosts(
                        pageNumber,
                        pageSize,
                        sortBy,
                        sortDir
                );

        return ResponseEntity.ok(posts);
    }

    // GET POST BY ID
    @GetMapping("/posts/{postId}")
    public ResponseEntity<PostDto> getPostById(
            @PathVariable Integer postId) {

        PostDto postDto =
                this.postService.getPostById(postId);

        return ResponseEntity.ok(postDto);
    }

    // UPDATE POST
    @PutMapping("/posts/{postId}")
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isOwner(#postId, authentication.name)")
    public ResponseEntity<PostDto> updatePost(
            @Valid @RequestBody PostDto postDto,
            @PathVariable Integer postId) {

        PostDto updatedPost =
                this.postService.updatePost(
                        postDto,
                        postId
                );

        return ResponseEntity.ok(updatedPost);
    }

    // DELETE POST
    @DeleteMapping("/posts/{postId}")
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isOwner(#postId, authentication.name)")
    public ResponseEntity<String> deletePost(
            @PathVariable Integer postId) {

        this.postService.deletePost(postId);

        return ResponseEntity.ok(
                "Post deleted successfully"
        );
    }

    // GET POSTS BY USER
    @GetMapping("/Users/{userId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByUser(
            @PathVariable Integer userId) {

        List<PostDto> posts =
                this.postService.getPostsByUser(userId);

        return ResponseEntity.ok(posts);
    }

    // GET POSTS BY CATEGORY
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDto>> getPostsByCategory(
            @PathVariable Integer categoryId) {

        List<PostDto> posts =
                this.postService.getPostsByCategory(categoryId);

        return ResponseEntity.ok(posts);
    }
    
    
    @GetMapping("/posts/search")
    public ResponseEntity<List<PostDto>> searchPosts(
            @RequestParam String keyword) {

        List<PostDto> posts =
                this.postService.searchPosts(keyword);

        return ResponseEntity.ok(posts);
    }
    
    
    // Image Upload endpoint
    
    @PostMapping("/post/image/upload/{postId}")
    @PreAuthorize("hasRole('ADMIN') or @postSecurity.isOwner(#postId, authentication.name)")
    public ResponseEntity<PostDto> uploadPostImage(
            @RequestParam("image") MultipartFile image,
            @PathVariable Integer postId) {

        PostDto postDto =
                this.postService.getPostById(postId);

        String fileName =
                this.fileService.uploadImage(
                        path,
                        image
                );

        postDto.setImage(fileName);

        PostDto updatedPost =
                this.postService.updatePost(
                        postDto,
                        postId
                );

        return ResponseEntity.ok(updatedPost);
    }
    
 // Get Image
    @GetMapping("/post/image/{imageName}")
    public ResponseEntity<InputStreamResource> downloadImage(
            @PathVariable String imageName) {

        InputStream inputStream =
                this.fileService.getResource(
                        path,
                        imageName
                );

        InputStreamResource resource =
                new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
    }
    
    
    
}