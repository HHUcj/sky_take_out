package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.service.WorkSpaceService;
import com.sky.vo.BusinessDataVO;
import com.sky.vo.DishOverViewVO;
import com.sky.vo.OrderOverViewVO;
import com.sky.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * ClassName: WorkSpaceController
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/22 - 下午10:07
 * @Version: v1.0
 */
@RestController
@RequestMapping("/admin/workspace")
public class WorkSpaceController {

    @Autowired
    private WorkSpaceService workSpaceService;

    @GetMapping("/businessData")
    public Result<BusinessDataVO> getBusinessData() {
        LocalDate now = LocalDate.now();
        LocalDateTime begin = LocalDateTime.of(now, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(now, LocalTime.MAX);
        BusinessDataVO businessDataVO = workSpaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    @GetMapping("/overviewSetmeals")
    public Result<SetmealOverViewVO> getOverviewSetmeals() {
        SetmealOverViewVO setmeal = workSpaceService.getOverviewSetmeals();
        return Result.success(setmeal);
    }

    @GetMapping("/overviewDishes")
    public Result<DishOverViewVO> getOverviewDishes() {
        DishOverViewVO dish = workSpaceService.getOverviewDishes();
        return Result.success(dish);
    }

    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> getOverviewOrders() {
        OrderOverViewVO orderOverViewVO = workSpaceService.getOverviewOrders();
        return Result.success(orderOverViewVO);
    }
}
