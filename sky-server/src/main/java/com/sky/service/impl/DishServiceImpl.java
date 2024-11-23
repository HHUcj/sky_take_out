package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DishServiceImpl
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/17 - 上午1:46
 * @Version: v1.0
 */
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.addDish(dish);

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavorMapper.addDishFlavor(flavors);
        }
    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        Integer page = dishPageQueryDTO.getPage();
        Integer pageSize = dishPageQueryDTO.getPageSize();
        Integer pageNo = (page-1) * pageSize;
        Integer categoryId = dishPageQueryDTO.getCategoryId();
        Integer status = dishPageQueryDTO.getStatus();
        String name = dishPageQueryDTO.getName();

        List<DishVO> dishVOList = dishMapper.page(name, status, categoryId, pageNo, pageSize);
        Long count = dishMapper.count(name, status, categoryId);

        PageResult pageResult = new PageResult();
        pageResult.setRecords(dishVOList);
        pageResult.setTotal(count);

        return pageResult;
    }

    @Override
    @Transactional
    public void batchDelDish(List<Long> ids) {
        List<Dish> dishList = dishMapper.getStatusByIds(ids);
        for (Dish dish : dishList) {
            if (StatusConstant.ENABLE.equals(dish.getStatus())) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }

        Long count = setMealDishMapper.getFromSetMealDish(ids);
        if (count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        dishMapper.delDish(ids);
        dishFlavorMapper.delDishFlavor(ids);
    }

    @Override
    public DishVO getDishById(Long id) {
        Dish dish = dishMapper.getDishById(id);
        List<DishFlavor> dishFlavor = dishFlavorMapper.getDishFlavorById(id);

        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(dishFlavor);

        return dishVO;
    }

    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.updateDish(dish);
        dishFlavorMapper.delDishFlavorById(dishDTO.getId());
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavorMapper.addDishFlavor(flavors);
        }
    }

    @Override
    public void startOrStop(Integer status, Long id) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.updateDish(dish);
    }

    @Override
    public List<Dish> getDishByCategoryId(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);
    }

    @Override
    public List<DishVO> getDishesByCategoryId(Integer categoryId) {
        List<DishVO> dishVOList = dishMapper.getDishesByCategoryId(categoryId);

        for (DishVO dishVO : dishVOList) {
            List<DishFlavor> dishFlavorList = dishFlavorMapper.getFalvorsById(dishVO.getId());
            dishVO.setFlavors(dishFlavorList);
        }
        return dishVOList;
    }
}