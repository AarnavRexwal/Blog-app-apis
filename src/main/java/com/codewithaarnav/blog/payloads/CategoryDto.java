package com.codewithaarnav.blog.payloads;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CategoryDto {

    private Integer categoryId;

    @NotBlank(message = "Category title cannot be empty")
    @Size(min = 3, max = 100, message = "Category title must be between 3 and 100 characters")
    private String categoryTitle;

    @NotBlank(message = "Category description cannot be empty")
    @Size(min = 10, max = 500, message = "Category description must be between 10 and 500 characters")
    private String categoryDescription;
}