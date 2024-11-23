package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: CategoryController
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/15 - 下午11:04
 * @Version: v1.0
 */
@Slf4j
@RestController("adminCategoryController")
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageResult pageResult = new PageResult();

        Integer page = categoryPageQueryDTO.getPage();
        Integer pageSize = categoryPageQueryDTO.getPageSize();
        Integer type = categoryPageQueryDTO.getType();
        String name = categoryPageQueryDTO.getName();

        pageResult.setTotal(categoryService.count());
        pageResult.setRecords(categoryService.page(name, type, page, pageSize));
        return Result.success(pageResult);
    }

    @GetMapping("/list")
    public Result<List<Category>> getCategoryByType(CategoryPageQueryDTO categoryPageQueryDTO) {
        List<Category> list = categoryService.getCategoryByType(categoryPageQueryDTO.getType());
        return Result.success(list);
    }

    @PutMapping
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        categoryService.startOrStop(id, status);
        return Result.success();
    }

    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result delCatgoryById(Long id) {
        categoryService.delCatgoryById(id);
        return Result.success();
    }
}
