package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.DishMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: WorkSpaceServiceImpl
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/22 - 下午10:07
 * @Version: v1.0
 */
@Service
public class WorkSpaceServiceImpl implements WorkSpaceService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end) {
        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);

        // 总订单数
        Integer orderCount = orderMapper.getOrderCnt(map);
        orderCount = orderCount == null ? 0 : orderCount;

        map.put("status", Orders.COMPLETED);

        // 总金额
        Double turnover = orderMapper.getAmount(map);
        turnover = turnover == null ? 0 : turnover;

        // 有效订单数
        Integer validOrderCount = orderMapper.getOrderCnt(map);
        validOrderCount = validOrderCount == null ? 0 : validOrderCount;

        // 新增用户数
        Integer newUsers = userMapper.getUserByCreateTime(map);
        newUsers = newUsers == null ? 0 : newUsers;

        Double orderCompletionRate = orderCount == 0.0 ? 0.0 : (validOrderCount * 1.0 / orderCount);

        Double unitPrice = validOrderCount == 0 ? 0 : (turnover / validOrderCount);

        return new BusinessDataVO(turnover, validOrderCount, orderCompletionRate, unitPrice, newUsers);
    }

    @Override
    public SetmealOverViewVO getOverviewSetmeals() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 1);
        Integer sold = setmealMapper.getNum(map);
        map.put("status", 0);
        Integer discontinued = setmealMapper.getNum(map);
        return new SetmealOverViewVO(sold, discontinued);
    }

    @Override
    public DishOverViewVO getOverviewDishes() {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 1);
        Integer sold = dishMapper.getNum(map);
        map.put("status", 0);
        Integer discontinued = dishMapper.getNum(map);
        return new DishOverViewVO(sold, discontinued);
    }

    @Override
    public OrderOverViewVO getOverviewOrders() {
        Map<String, Object> map = getStringObjectMap();
        map.put("status", Orders.REFUND);
        Integer orderCount = orderMapper.getOrderCnt(map);
        map.put("status", Orders.CONFIRMED);
        Integer confirmedCount = orderMapper.getOrderCnt(map);
        map.put("status", Orders.COMPLETED);
        Integer completedCount = orderMapper.getOrderCnt(map);
        map.put("status", Orders.CANCELLED);
        Integer canceledCount = orderMapper.getOrderCnt(map);
        map.put("status", null);
        Integer totalCnt = orderMapper.getOrderCnt(map);

        return new OrderOverViewVO(orderCount, confirmedCount, completedCount, canceledCount, totalCnt);
    }

    private static Map<String, Object> getStringObjectMap() {
        LocalDate now = LocalDate.now();
        LocalDateTime begin = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(now, LocalTime.MAX);

        Map<String, Object> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        return map;
    }
}