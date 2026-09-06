package com.codewithaarnav.blog.payloads;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostDto {

    private Integer postId;

    @NotBlank(message = "Post title cannot be empty")
    @Size(min = 3, max = 100,
          message = "Post title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Post content cannot be empty")
    private String content;
    
    private int pageSize;
    
    private int totalELements;
    
    private int totalPages;
    
    private boolean lastPage;

    private String image;

    private LocalDateTime postDate;
}