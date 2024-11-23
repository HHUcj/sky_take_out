package com.sky.mapper;

import com.sky.annotion.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: DishFlavorMapper
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/17 - 上午1:50
 * @Version: v1.0
 */
@Mapper
public interface DishFlavorMapper {
    void addDishFlavor(List<DishFlavor> flavors);

    void delDishFlavor(List<Long> ids);

    List<DishFlavor> getDishFlavorById(Long id);

    void delDishFlavorById(Long id);

    List<DishFlavor> getFalvorsById(Long id);
}
