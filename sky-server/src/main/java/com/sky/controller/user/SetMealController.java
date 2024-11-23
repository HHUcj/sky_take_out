package com.sky.controller.user;

import com.sky.result.Result;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ClassName: SetMealController
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/19 - 下午4:04
 * @Version: v1.0
 */
@RestController("userSetMealController")
@RequestMapping("/user/setmeal")
@Slf4j
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @GetMapping("/list")
    @Cacheable(cacheNames = "setmealCache", key = "#categoryId")
    public Result<List<SetmealVO>> getSetMealByCategoryId(Integer categoryId) {
        List<SetmealVO> setmealVOList = setMealService.getSetMealByCategoryId(categoryId);
        return Result.success(setmealVOList);
    }

    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> getDishesBySetMealId(@PathVariable Long id) {
        List<DishItemVO> dishItemVOList = setMealService.getDishesBySetMealId(id);
        return Result.success(dishItemVOList);
    }
}
