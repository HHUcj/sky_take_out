package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.service.SetMealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ClassName: SetMealServiceImpl
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/17 - 下午10:24
 * @Version: v1.0
 */
@Service
public class SetMealServiceImpl implements SetMealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    public List<SetmealVO> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        int page = setmealPageQueryDTO.getPage();
        int pageSize = setmealPageQueryDTO.getPageSize();
        int pageNo = (page - 1) * pageSize;
        String name = setmealPageQueryDTO.getName();
        Integer categoryId = setmealPageQueryDTO.getCategoryId();
        Integer status = setmealPageQueryDTO.getStatus();

        return setmealMapper.page(name, categoryId, status, pageNo, pageSize);
    }

    @Override
    public Long count(SetmealPageQueryDTO setmealPageQueryDTO) {
        String name = setmealPageQueryDTO.getName();
        Integer categoryId = setmealPageQueryDTO.getCategoryId();
        Integer status = setmealPageQueryDTO.getStatus();
        return setmealMapper.count(name, categoryId, status);
    }

    @Override
    @Transactional
    public void updateSetMeal(SetmealDTO setmealDTO) {
        Long id = setmealDTO.getId();
        setMealDishMapper.delDishInCurSetMeal(id);

        List<SetmealDish> setmealDishesList = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishesList) {
            setmealDish.setSetmealId(id);
        }
        setMealDishMapper.addDishInCurSetMeal(setmealDishesList);

        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.updateSetMeal(setmeal);
    }

    @Override
    public SetmealVO getSetMealById(Long id) {
        SetmealVO setmealVO = setmealMapper.getSetMealById(id);
        List<SetmealDish> list = setMealDishMapper.getSetmealDishById(id);
        setmealVO.setSetmealDishes(list);
        return setmealVO;
    }

    @Override
    public void startOrstop(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        if (status == StatusConstant.DISABLE) {
            setmealMapper.updateSetMeal(setmeal);
        } else if (status == StatusConstant.ENABLE) {
            List<Dish> dishList = setMealDishMapper.getAllDishStatus(id);
            for (Dish dish : dishList) {
                if (dish.getStatus() == StatusConstant.DISABLE) {
                    throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
            setmealMapper.updateSetMeal(setmeal);
        }
    }

    @Override
    public void batchDelSetMeal(List<Long> ids) {
        List<Integer> statusList = setmealMapper.getStatusByIds(ids);
        boolean flag = true;
        for (Integer status : statusList) {
            if (status == StatusConstant.ENABLE) {
                flag = false;
                break;
            }
        }
        if (!flag) {
            throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
        }
        setmealMapper.batchDelSetMeal(ids);
    }

    @Override
    @Transactional
    public void addSetMeal(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        setmealMapper.addSetMeal(setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
        }
        setMealDishMapper.addDishInCurSetMeal(setmealDishes);
    }

    @Override
    public List<SetmealVO> getSetMealByCategoryId(Integer categoryId) {
        return setmealMapper.getSetMealByCategoryId(categoryId);
    }

    @Override
    public List<DishItemVO> getDishesBySetMealId(Long id) {
        return setmealMapper.getDishesBySetMealId(id);
    }
}