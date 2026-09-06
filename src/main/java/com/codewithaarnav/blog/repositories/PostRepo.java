package com.codewithaarnav.blog.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codewithaarnav.blog.entities.Post;
import com.codewithaarnav.blog.entities.User;
import com.codewithaarnav.blog.entities.Category;

public interface PostRepo extends JpaRepository<Post, Integer> {
	List<Post> findByUser(User user);
	List<Post> findByCategory(Category category);
	
	List<Post> findByTitleContainingIgnoreCase(String title);
}