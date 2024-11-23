package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: CategoryServiceImpl
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/15 - 下午11:19
 * @Version: v1.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public long count() {
        return categoryMapper.count();
    }

    @Override
    public List<Category> page(String name, Integer type, Integer page, Integer pageSize) {
        Integer pageNo = (page - 1) * pageSize;
        return categoryMapper.page(name, type, pageNo, pageSize);
    }

    @Override
    public List<Category> getCategoryByType(Integer type) {
        return categoryMapper.getCategoryByType(type);
    }

    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);

        categoryMapper.updateCategory(category);
    }

    @Override
    public void startOrStop(Long id, Integer status) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                .build();

        categoryMapper.updateCategory(category);
    }

    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = Category.builder()
                .type(categoryDTO.getType())
                .name(categoryDTO.getName())
                .sort(categoryDTO.getSort())
                .status(0)
                .build();

        categoryMapper.addCategory(category);
    }

    @Override
    public void delCatgoryById(Long id) {
        Integer count = dishMapper.getByCategoryId(id);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        count = setmealMapper.getByCategoryId(id);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.delCatgoryById(id);
    }

    @Override
    public List<Category> getCategory() {
        return categoryMapper.getCategory();
    }
}