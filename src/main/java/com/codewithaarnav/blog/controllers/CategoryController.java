package com.codewithaarnav.blog.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.codewithaarnav.blog.payloads.CategoryDto;
import com.codewithaarnav.blog.services.CategoryService;

import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // CREATE CATEGORY
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/")
    public ResponseEntity<CategoryDto> createCategory(
           @Valid @RequestBody CategoryDto categoryDto) {

        CategoryDto createdCategory =
                this.categoryService.createCategory(categoryDto);

        return new ResponseEntity<>(
                createdCategory,
                HttpStatus.CREATED
        );
    }

    // UPDATE CATEGORY
    @PutMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Integer categoryId) {

        Object updatedCategory =
                this.categoryService.updateCategory(
                        categoryDto,
                        categoryId
                );

        return ResponseEntity.ok(updatedCategory);
    }

    // DELETE CATEGORY
    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(
            @PathVariable Integer categoryId) {

        this.categoryService.deleteCategory(categoryId);

        return ResponseEntity.ok(
                "Category deleted successfully"
        );
    }

    // GET CATEGORY BY ID
    @GetMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> getCategory(
            @PathVariable Integer categoryId) {

        CategoryDto categoryDto =
                this.categoryService.getCategory(categoryId);

        return ResponseEntity.ok(categoryDto);
    }

    // GET ALL CATEGORIES
    @GetMapping("/")
    public ResponseEntity<List<CategoryDto>> getCategories() {

        List<CategoryDto> categories =
                this.categoryService.getCategories();

        return ResponseEntity.ok(categories);
    }
}