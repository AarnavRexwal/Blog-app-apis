package com.codewithaarnav.blog.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codewithaarnav.blog.entities.Category;
import com.codewithaarnav.blog.exceptions.ResourceNotFoundException;
import com.codewithaarnav.blog.payloads.CategoryDto;
import com.codewithaarnav.blog.repositories.CategoryRepo;
import com.codewithaarnav.blog.services.CategoryService;

@Service
public class CategoryServiceimpl implements CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private ModelMapper modelMapper;

    // CREATE
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {

        Category cat = this.modelMapper.map(categoryDto, Category.class);

        Category addedCat = this.categoryRepo.save(cat);

        return this.modelMapper.map(addedCat, CategoryDto.class);
    }

    // UPDATE
    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Integer categoryId) {

        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category",
                                "Category Id",
                                categoryId
                        ));

        cat.setCategoryTitle(categoryDto.getCategoryTitle());
        cat.setCategoryDescription(categoryDto.getCategoryDescription());

        Category updatedCat = this.categoryRepo.save(cat);

        return this.modelMapper.map(updatedCat, CategoryDto.class);
    }

    // DELETE
    @Override
    public void deleteCategory(Integer categoryId) {

        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category",
                                "Category Id",
                                categoryId
                        ));

        this.categoryRepo.delete(cat);
    }

    // GET BY ID
    @Override
    public CategoryDto getCategory(Integer categoryId) {

        Category cat = this.categoryRepo.findById(categoryId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category",
                                "Category Id",
                                categoryId
                        ));

        return this.modelMapper.map(cat, CategoryDto.class);
    }

    // GET ALL
    @Override
    public List<CategoryDto> getCategories() {

        List<Category> categories = this.categoryRepo.findAll();

        List<CategoryDto> categoryDtos = categories.stream()
                .map(category -> this.modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());

        return categoryDtos;
    }
}