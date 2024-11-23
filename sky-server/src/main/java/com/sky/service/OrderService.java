package com.sky.service;

import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: OrderService
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午9:28
 * @Version: v1.0
 */
public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);

    Long count(Integer status);

    List<OrderVO> getHistoryOrders(Integer page, Integer pageSize, Integer status);

    OrderVO getOrderDetail(Long id);

    void cancel(Long id);

    void repetition(Long id);

    Long orderCount(String beginTime, String endTime, String number, String phone, Integer status);

    List<OrderVO> conditionSearch(String beginTime, String endTime, String number, Integer page, Integer pageSize, String phone, Integer status);

    OrderStatisticsVO statistics();

    OrderVO details(Long id);

    void confirm(OrdersDTO ordersDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    void delivery(Long id);

    void complete(Long id);

    void reminder(Long id);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO);
}
