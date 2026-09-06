package com.codewithaarnav.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.codewithaarnav.blog.entities.Category;
import com.codewithaarnav.blog.entities.Post;
import com.codewithaarnav.blog.entities.User;
import com.codewithaarnav.blog.exceptions.ResourceNotFoundException;
import com.codewithaarnav.blog.payloads.PostDto;
import com.codewithaarnav.blog.repositories.CategoryRepo;
import com.codewithaarnav.blog.repositories.PostRepo;
import com.codewithaarnav.blog.repositories.UserRepo;
import com.codewithaarnav.blog.services.FileService;
import com.codewithaarnav.blog.services.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class PostServiceimpl implements PostService {

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private FileService fileService;

    // CREATE POST
    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "User Id",
                                userId
                        ));

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category",
                                "Category Id",
                                categoryId
                        ));

        Post post = this.modelMapper.map(postDto, Post.class);

        post.setUser(user);
        post.setCategory(category);

        Post savedPost = this.postRepo.save(post);

        return this.modelMapper.map(savedPost, PostDto.class);
    }

    // UPDATE POST
    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post",
                                "Post Id",
                                postId
                        ));

        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImage(postDto.getImage());

        Post updatedPost = this.postRepo.save(post);

        return this.modelMapper.map(updatedPost, PostDto.class);
    }

    // DELETE POST
    @Override
    public void deletePost(Integer postId) {

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post",
                                "Post Id",
                                postId
                        ));

        this.postRepo.delete(post);
    }

    // GET POST BY ID
    @Override
    public PostDto getPostById(Integer postId) {

        Post post = this.postRepo.findById(postId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Post",
                                "Post Id",
                                postId
                        ));

        return this.modelMapper.map(post, PostDto.class);
    }

    // GET ALL POSTS
    @Override
    public List<PostDto> getAllPosts() {

        List<Post> posts = this.postRepo.findAll();

        return posts.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    // GET POSTS BY USER
    @Override
    public List<PostDto> getPostsByUser(Integer userId) {

        User user = this.userRepo.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "User Id",
                                userId
                        ));

        List<Post> posts = this.postRepo.findByUser(user);

        return posts.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }

    // GET POSTS BY CATEGORY
    @Override
    public List<PostDto> getPostsByCategory(Integer categoryId) {

        Category category = this.categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category",
                                "Category Id",
                                categoryId
                        ));

        List<Post> posts = this.postRepo.findByCategory(category);

        return posts.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }
    
 // GET ALL POSTS WITH PAGINATION AND SORTING
    @Override
    public Page<PostDto> getAllPosts(
            int pageNumber,
            int pageSize,
            String sortBy,
            String sortDir) {
    	
//    	By using Ternary
//    	Sort sort = sortDir.equalsIgnoreCase("desc")
//        ? Sort.by(sortBy).descending()
//        : Sort.by(sortBy).ascending();
    	
//    	          |
    	          

        Sort sort = null;
        if(sortDir.equalsIgnoreCase("asc")) {
        	sort=sort.by(sortBy).ascending();
        }else {
        	sort = Sort.by(sortBy).descending();
        }

        Pageable pageable = PageRequest.of(
                pageNumber,
                pageSize,
                sort
        );

        Page<Post> pagePost = this.postRepo.findAll(pageable);

        return pagePost.map(
                post -> this.modelMapper.map(post, PostDto.class)
        );
    }        
        
        //Search posts
    @Override
    public List<PostDto> searchPosts(String keyword) {

        List<Post> posts =
                this.postRepo.findByTitleContainingIgnoreCase(keyword);

        return posts.stream()
                .map(post -> this.modelMapper.map(post, PostDto.class))
                .collect(Collectors.toList());
    }
	
}