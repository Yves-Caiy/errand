package com.axin.system.service;

import com.axin.system.domain.OrderDispatchLog;

import java.util.List;

public interface IOrderDispatchLogService {
    int insertOrderDispatchLog(OrderDispatchLog log);

    List<OrderDispatchLog> selectOrderDispatchLogList(OrderDispatchLog query);

    List<OrderDispatchLog> selectLogsByOrderId(Long orderId);

    int updateStatus(Long id, Integer status);

    int updateStatusByOrderRound(Long orderId, Integer round, Integer status);

    int markOthersAccepted(Long orderId, Long runnerUserId);

    int markAccepted(Long orderId, Long runnerUserId);

    int countRounds(Long orderId);

    /** 标记来单提醒已读：logIds 为空时表示该接单员全部未读记录 */
    int markNoticeRead(Long runnerUserId, List<Long> logIds);

    List<Long> selectPendingTakeoutOrderIds();
}
