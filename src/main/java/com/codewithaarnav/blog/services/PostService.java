package com.codewithaarnav.blog.services;

import java.util.List;

import org.springframework.data.domain.Page;

import com.codewithaarnav.blog.payloads.PostDto;

public interface PostService {

    // Create Post
    PostDto createPost(PostDto postDto, Integer userId, Integer categoryId);

    // Update Post
    PostDto updatePost(PostDto postDto, Integer postId);

    // Delete Post
    void deletePost(Integer postId);

    // Get Post by ID
    PostDto getPostById(Integer postId);

    // Get All Posts
    List<PostDto> getAllPosts();

    // Get Posts by User
    List<PostDto> getPostsByUser(Integer userId);

    // Get Posts by Category
    List<PostDto> getPostsByCategory(Integer categoryId);

    // Get All Posts with Pagination and Sorting
    Page<PostDto> getAllPosts(int pageNumber, int pageSize, String sortBy, String sortDir);
    
    //Search Posts
    List<PostDto> searchPosts(String keyword);
}