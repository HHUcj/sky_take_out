package com.sky.controller.user;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: CategoryController
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/19 - 下午3:46
 * @Version: v1.0
 */
@RestController("userCategoryController")
@RequestMapping("/user/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> getCategory() {
//        List<Category> categoryList = categoryService.getCategoryByType(type);
        List<Category> categoryList = categoryService.getCategory();
        return Result.success(categoryList);
    }
}
