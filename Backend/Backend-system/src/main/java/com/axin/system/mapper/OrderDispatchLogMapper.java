package com.axin.system.mapper;

import com.axin.system.domain.OrderDispatchLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderDispatchLogMapper {
    int insertOrderDispatchLog(OrderDispatchLog log);

    List<OrderDispatchLog> selectOrderDispatchLogList(OrderDispatchLog query);

    List<OrderDispatchLog> selectLogsByOrderId(@Param("orderId") Long orderId);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int updateStatusByOrderRound(@Param("orderId") Long orderId, @Param("round") Integer round, @Param("status") Integer status);

    /** 除接单人外的本届已推送记录置为「他人已接」 */
    int markOthersAccepted(@Param("orderId") Long orderId, @Param("runnerUserId") Long runnerUserId);

    /** 把某接单员的推送记录标记为已接受 */
    int markAccepted(@Param("orderId") Long orderId, @Param("runnerUserId") Long runnerUserId);

    /** 统计某单已产生记录的轮次数 */
    int countRounds(@Param("orderId") Long orderId);

    /** 标记来单提醒已读：logIds 为空时表示该接单员全部未读记录 */
    int markNoticeRead(@Param("runnerUserId") Long runnerUserId, @Param("logIds") List<Long> logIds);

    /** 有待派单记录的待接单外卖订单 */
    List<Long> selectPendingTakeoutOrderIds();
}
