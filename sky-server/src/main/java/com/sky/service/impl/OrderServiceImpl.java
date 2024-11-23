package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.utils.TimeUtils;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import com.sky.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassName: OrderServiceImpl
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午9:28
 * @Version: v1.0
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {
        Long addressBookId = ordersSubmitDTO.getAddressBookId();
        AddressBook addressBook = addressBookMapper.getAddressById(addressBookId);
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }

        Long userId = BaseContext.getCurrentId();
        List<ShoppingCart> shoppingCart = shoppingCartMapper.getShoppingCart(userId);
        if (shoppingCart == null || shoppingCart.isEmpty()) {
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(0);
        orders.setUserName(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(addressBook.getDetail());
        orders.setConsignee(addressBook.getConsignee());

        // 主键回显
        orderMapper.addOrder(orders);

        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCart) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart, orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        orderMapper.batchAddOrderDetail(orderDetailList);

        shoppingCartMapper.delShoppingCart(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("type", 1);
        map.put("orderId", orders.getId());
        map.put("content", "订单号" + orders.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(map));

        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .build();
        return orderSubmitVO;
    }

    @Override
    public Long count(Integer status) {
        return orderMapper.count(status);
    }

    @Override
    public List<OrderVO> getHistoryOrders(Integer page, Integer pageSize, Integer status) {
        Integer pageNo = (page - 1) * pageSize;
        Long userId = BaseContext.getCurrentId();
        return orderMapper.getHistoryOrders(status, userId, pageNo, pageSize);
    }

    @Override
    public OrderVO getOrderDetail(Long id) {
        return orderMapper.getOrderDetail(id);
    }

    @Override
    public void cancel(Long id) {
        orderMapper.cancel(id, Orders.CANCELLED);
    }

    @Override
    public void repetition(Long id) {
        OrderVO orderDetail = orderMapper.getOrderDetail(id);

        List<OrderDetail> orderDetailList = orderDetail.getOrderDetailList();
        List<ShoppingCart> shoppingCartList = new ArrayList<>();
        for (OrderDetail orderDetail1 : orderDetailList) {
            ShoppingCart shoppingCart = new ShoppingCart();
            shoppingCart.setUserId(BaseContext.getCurrentId());
            BeanUtils.copyProperties(orderDetail1, shoppingCart);
            shoppingCartList.add(shoppingCart);
        }

        shoppingCartMapper.batchAddShoppingCartMapper(shoppingCartList);
    }

    @Override
    public Long orderCount(String beginTime, String endTime, String number, String phone, Integer status) {

        return orderMapper.orderCount(TimeUtils.parseLocalDateTime(beginTime), TimeUtils.parseLocalDateTime(endTime), number, phone, status);
    }

    @Override
    public List<OrderVO> conditionSearch(String beginTime, String endTime, String number, Integer page, Integer pageSize, String phone, Integer status) {
        Integer pageNo = (page - 1) * pageSize;
        return orderMapper.conditionSearch(TimeUtils.parseLocalDateTime(beginTime), TimeUtils.parseLocalDateTime(endTime), number, phone, status, pageNo, pageSize);
    }

    @Override
    public OrderStatisticsVO statistics() {
        Integer toBeConfirmed = orderMapper.statisticsToBeConfirmed();
        Integer confirmed = orderMapper.statisticsConfirmed();
        Integer deliveryInProgress = orderMapper.statisticsDeliveryInProgress();
        return new OrderStatisticsVO(toBeConfirmed, confirmed, deliveryInProgress);
    }

    @Override
    public OrderVO details(Long id) {
        return orderMapper.getOrderDetail(id);
    }

    @Override
    public void confirm(OrdersDTO ordersDTO) {
        orderMapper.confirm(ordersDTO);
    }

    @Override
    public void rejection(OrdersRejectionDTO ordersRejectionDTO) {
        orderMapper.rejection(ordersRejectionDTO);
    }

    @Override
    public void cancelOrder(OrdersCancelDTO ordersCancelDTO) {
        orderMapper.cancelOrder(ordersCancelDTO);
    }

    @Override
    public void delivery(Long id) {
        orderMapper.delivery(id);
    }

    @Override
    public void complete(Long id) {
        orderMapper.complete(id);
    }

    @Override
    public void reminder(Long id) {
        OrderVO orderDetail = orderMapper.getOrderDetail(id);
        if (orderDetail == null) {
            throw new OrderBusinessException(MessageConstant.ORDER_NOT_FOUND);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("type", 2);
        map.put("orderId", id);
        map.put("content", "订单号" + orderDetail.getNumber());
        webSocketServer.sendToAllClient(JSON.toJSONString(map));
    }

    @Override
    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) {
        String orderNumber = ordersPaymentDTO.getOrderNumber();
        Orders orders = orderMapper.getOrderByNumber(orderNumber);
        orders.setPayStatus(Orders.PAID);
        orderMapper.update(orders);
        return new OrderPaymentVO();
    }
}