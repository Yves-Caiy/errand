package com.axin.system.mapper;

import com.axin.system.domain.OrderMain;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMainMapper {
    OrderMain selectOrderMainById(@Param("id") Long id);

    List<OrderMain> selectOrderMainList(OrderMain order);

    int insertOrderMain(OrderMain order);

    int updateOrderMain(OrderMain order);

    int acceptOrder(@Param("id") Long id, @Param("runnerId") Long runnerId);
}
