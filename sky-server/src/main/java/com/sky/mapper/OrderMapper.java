package com.sky.mapper;

import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersCancelDTO;
import com.sky.dto.OrdersDTO;
import com.sky.dto.OrdersRejectionDTO;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.result.PageResult;
import com.sky.vo.OrderStatisticsVO;
import com.sky.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * ClassName: OrderMapper
 * Description:
 *
 * @Author: 陈杰
 * @Create: 2024/11/20 - 下午9:28
 * @Version: v1.0
 */
@Mapper
public interface OrderMapper {
    void addOrder(Orders orders);

    void batchAddOrderDetail(List<OrderDetail> orderDetailList);

    Long count(Integer status);

    List<OrderDetail> getFromOrderDetailById(Long id);

    List<OrderVO> getHistoryOrders(@Param("status") Integer status, @Param("userId") Long usrId, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    OrderVO getOrderDetail(Long id);

    void cancel(Long id, Integer cancelled);

    Long orderCount(LocalDateTime beginTime, LocalDateTime endTime, String number, String phone, Integer status);

    List<OrderVO> conditionSearch(LocalDateTime beginTime, LocalDateTime endTime, String number, String phone, Integer status, Integer pageNo, Integer pageSize);

    Integer statisticsToBeConfirmed();

    Integer statisticsConfirmed();

    Integer statisticsDeliveryInProgress();

    void confirm(OrdersDTO ordersDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    void delivery(Long id);

    void complete(Long id);

    List<Orders> getAllTimeOutOrders(Integer status, LocalDateTime time);

    void update(Orders order);

    Double getAmount(Map<String, Object> map);

    Integer getOrderCnt(Map<String, Object> map);

    List<GoodsSalesDTO> getTop10Report(LocalDateTime beginTime, LocalDateTime endTime);

    Orders getOrderByNumber(String orderNumber);
}
