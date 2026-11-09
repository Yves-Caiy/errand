package com.axin.system.mapper;

import com.axin.system.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    List<OrderItem> selectOrderItemListByOrderId(@Param("orderId") Long orderId);

    int insertOrderItem(OrderItem item);
}
