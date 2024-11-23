package com.sky.service;

import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;

import java.time.LocalDateTime;

/**
 * ClassName: WorkSpaceService
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/22 - 下午10:07
 * @Version: v1.0
 */
public interface WorkSpaceService {

    SetmealOverViewVO getOverviewSetmeals();

    DishOverViewVO getOverviewDishes();

    OrderOverViewVO getOverviewOrders();

    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);
}
