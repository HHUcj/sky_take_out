package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.vo.DishVO;

import java.util.List;

/**
 * ClassName: DishService
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/17 - 上午1:45
 * @Version: v1.0
 */
public interface DishService {
    void addDish(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    void batchDelDish(List<Long> ids);

    DishVO getDishById(Long id);

    void updateDish(DishDTO dishDTO);

    void startOrStop(Integer status, Long id);

    List<Dish> getDishByCategoryId(Long categoryId);

    List<DishVO> getDishesByCategoryId(Integer categoryId);
}
