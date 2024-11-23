package com.sky.mapper;

import com.sky.annotion.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: CategoryMapper
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/15 - 下午11:27
 * @Version: v1.0
 */
@Mapper
public interface CategoryMapper {

    long count();

    List<Category> page(String name, Integer type, Integer pageNo, Integer pageSize);

    List<Category> getCategoryByType(Integer type);

    @AutoFill(value = OperationType.UPDATE)
    void updateCategory(Category category);

    @AutoFill(value = OperationType.INSERT)
    void addCategory(Category category);

    void delCatgoryById(Long id);

    List<Category> getCategory();
}
