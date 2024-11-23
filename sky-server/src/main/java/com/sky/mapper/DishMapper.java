package com.sky.mapper;

import com.sky.annotion.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * ClassName: DishMapper
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/16 - 下午9:18
 * @Version: v1.0
 */
@Mapper
public interface DishMapper {

    Integer getByCategoryId(Long id);

    @AutoFill(value = OperationType.INSERT)
    void addDish(Dish dish);

    List<DishVO> page(String name, Integer status, Integer categoryId, Integer pageNo, Integer pageSize);

    Long count(String name, Integer status, Integer categoryId);

    List<Dish> getStatusByIds(List<Long> ids);

    void delDish(List<Long> ids);

    Dish getDishById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void updateDish(Dish dish);

    List<Dish> getDishByCategoryId(Long categoryId);

    List<DishVO> getDishesByCategoryId(Integer categoryId);

    Integer getNum(Map<String, Object> map);
}
