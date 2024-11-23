package com.sky.mapper;

import com.sky.entity.Dish;
import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: SetMealDishMapper
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/17 - 下午5:30
 * @Version: v1.0
 */
@Mapper
public interface SetMealDishMapper {
    Long getFromSetMealDish(List<Long> ids);

    List<SetmealDish> getSetmealDishById(Long id);

    void delDishInCurSetMeal(Long id);

    void addDishInCurSetMeal(List<SetmealDish> setmealDishesList);

    List<Dish> getAllDishStatus(Long id);
}