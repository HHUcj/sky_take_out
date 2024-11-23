package com.sky.mapper;

import com.sky.annotion.AutoFill;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * ClassName: SetmealMapper
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/16 - 下午9:18
 * @Version: v1.0
 */
@Mapper
public interface SetmealMapper {

    Integer getByCategoryId(Long id);

    List<SetmealVO> page(String name, Integer categoryId, Integer status, int pageNo, int pageSize);

    Long count(String name, Integer categoryId, Integer status);

    SetmealVO getSetMealById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void updateSetMeal(Setmeal setmeal);

    List<Integer> getStatusByIds(List<Long> ids);

    void batchDelSetMeal(List<Long> ids);

    @AutoFill(value = OperationType.INSERT)
    void addSetMeal(Setmeal setmeal);

    List<SetmealVO> getSetMealByCategoryId(Integer categoryId);

    List<DishItemVO> getDishesBySetMealId(Long id);

    Setmeal getById(Long setmealId);

    Integer getNum(Map<String, Object> map);
}
