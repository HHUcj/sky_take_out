package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: SetMealController
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/17 - 下午10:19
 * @Version: v1.0
 */
@RestController("adminSetMealController")
@RequestMapping("/admin/setmeal")
public class SetMealController {
    @Autowired
    private SetMealService setMealService;

    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        List<SetmealVO> record = setMealService.page(setmealPageQueryDTO);
        Long count = setMealService.count(setmealPageQueryDTO);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(count);
        pageResult.setRecords(record);
        return Result.success(pageResult);
    }

    @PutMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result updateSetMeal(@RequestBody SetmealDTO setmealDTO) {
        setMealService.updateSetMeal(setmealDTO);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<SetmealVO> getSetMealById(@PathVariable Long id) {
        SetmealVO setmealVO = setMealService.getSetMealById(id);
        return Result.success(setmealVO);
    }

    @PostMapping("/status/{status}")
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result startOrstop(@PathVariable Integer status, Long id) {
        setMealService.startOrstop(status, id);
        return Result.success();
    }

    @DeleteMapping
    @CacheEvict(cacheNames = "setmealCache", allEntries = true)
    public Result batchDelSetMeal(@RequestParam List<Long> ids) {
        setMealService.batchDelSetMeal(ids);
        return Result.success();
    }

    @PostMapping
    @CacheEvict(cacheNames = "setmealCache", key = "setmealDTO.getCategoryId")
    public Result addSetMeal(@RequestBody SetmealDTO setmealDTO) {
        setMealService.addSetMeal(setmealDTO);
        return Result.success();
    }
}
