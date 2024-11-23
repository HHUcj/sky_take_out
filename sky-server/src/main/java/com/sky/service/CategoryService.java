package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;

import java.util.List;

/**
 * ClassName: CategoryService
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/15 - 下午11:18
 * @Version: v1.0
 */
public interface CategoryService {
    long count();

    List page(String name, Integer type, Integer page, Integer pageSize);

    List getCategoryByType(Integer type);

    void updateCategory(CategoryDTO categoryDTO);

    void startOrStop(Long id, Integer status);

    void addCategory(CategoryDTO categoryDTO);

    void delCatgoryById(Long id);

    List<Category> getCategory();
}
