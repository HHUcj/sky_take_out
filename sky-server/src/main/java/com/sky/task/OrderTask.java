package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: OrderTask
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/21 - 下午11:16
 * @Version: v1.0
 */
@Component
public class OrderTask {

    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * * ")
    public void processTimeOutOrder() {
        LocalDateTime time = LocalDateTime.now().plusMinutes(-15);
        List<Orders> orders = orderMapper.getAllTimeOutOrders(Orders.PENDING_PAYMENT, time);

        for (Orders order : orders) {
            order.setStatus(Orders.CANCELLED);
            order.setCancelReason("订单超时未支付关闭");
            order.setCancelTime(LocalDateTime.now());
            orderMapper.update(order);
        }
    }

    @Scheduled(cron = "0 0 1 * * * ")
    public void processDeliveryOrders() {
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> orders = orderMapper.getAllTimeOutOrders(Orders.DELIVERY_IN_PROGRESS, time);

        for (Orders order : orders) {
            order.setStatus(Orders.CANCELLED);
            order.setCancelReason("订单超时关闭");
            order.setCancelTime(LocalDateTime.now());
            orderMapper.update(order);
        }
    }
}
