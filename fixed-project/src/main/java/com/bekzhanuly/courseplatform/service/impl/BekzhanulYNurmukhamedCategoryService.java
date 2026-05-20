package com.bekzhanuly.courseplatform.service.impl;

import com.bekzhanuly.courseplatform.dto.request.BekzhanulYNurmukhamedCategoryRequest;
import com.bekzhanuly.courseplatform.dto.response.BekzhanulYNurmukhamedCategoryResponse;
import com.bekzhanuly.courseplatform.entity.BekzhanulYNurmukhamedCategory;
import com.bekzhanuly.courseplatform.exception.BekzhanulYNurmukhamedExceptions;
import com.bekzhanuly.courseplatform.mapper.BekzhanulYNurmukhamedCategoryMapper;
import com.bekzhanuly.courseplatform.repository.BekzhanulYNurmukhamedCategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Category Service Implementation
 * Author: Bekzhanuly Nurmukhamed
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BekzhanulYNurmukhamedCategoryService {

    private final BekzhanulYNurmukhamedCategoryRepository categoryRepository;
    private final BekzhanulYNurmukhamedCategoryMapper categoryMapper;

    @Transactional(readOnly = true)
    public List<BekzhanulYNurmukhamedCategoryResponse> getAllCategories() {
        return categoryMapper.toResponseList(categoryRepository.findByIsActiveTrue());
    }

    @Transactional(readOnly = true)
    public BekzhanulYNurmukhamedCategoryResponse getCategoryById(Long id) {
        return categoryMapper.toResponse(findOrThrow(id));
    }

    @Transactional
    public BekzhanulYNurmukhamedCategoryResponse createCategory(
            BekzhanulYNurmukhamedCategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAlreadyExistsException(
                    "Category already exists: " + request.getName());
        }
        BekzhanulYNurmukhamedCategory category = BekzhanulYNurmukhamedCategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .iconUrl(request.getIconUrl())
                .isActive(true)
                .build();
        BekzhanulYNurmukhamedCategory saved = categoryRepository.save(category);
        log.info("Category created: {}", saved.getName());
        return categoryMapper.toResponse(saved);
    }

    @Transactional
    public BekzhanulYNurmukhamedCategoryResponse updateCategory(
            Long id, BekzhanulYNurmukhamedCategoryRequest request) {
        BekzhanulYNurmukhamedCategory category = findOrThrow(id);
        if (!category.getName().equals(request.getName())
                && categoryRepository.existsByName(request.getName())) {
            throw new BekzhanulYNurmukhamedExceptions.BekzhanulYNurmukhamedAlreadyExistsException(
                    "Category name already in use: " + request.getName());
        }
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        if (request.getIconUrl() != null) category.setIconUrl(request.getIconUrl());
        log.info("Category {} updated", id);
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public void deleteCategory(Long id) {
        BekzhanulYNurmukhamedCategory category = findOrThrow(id);
        category.setIsActive(false);
        categoryRepository.save(category);
        log.info("Category {} soft-deleted", id);
    }

    private BekzhanulYNurmukhamedCategory findOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new BekzhanulYNurmukhamedExceptions
                        .BekzhanulYNurmukhamedResourceNotFoundException("Category", id));
    }
}
