package com.sky.service;

import com.sky.annotion.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * ClassName: SetMealService
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/17 - 下午10:24
 * @Version: v1.0
 */
public interface SetMealService {
    List<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO);

    Long count(SetmealPageQueryDTO setmealPageQueryDTO);

    void updateSetMeal(SetmealDTO setmealDTO);

    SetmealVO getSetMealById(Long id);

    void startOrstop(Integer status, Long id);

    void batchDelSetMeal(List<Long> ids);

    void addSetMeal(SetmealDTO setmealDTO);

    List<SetmealVO> getSetMealByCategoryId(Integer categoryId);

    List<DishItemVO> getDishesBySetMealId(Long id);
}
