package com.axin.system.service.impl;

import com.axin.system.domain.OrderDispatchLog;
import com.axin.system.mapper.OrderDispatchLogMapper;
import com.axin.system.service.IOrderDispatchLogService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDispatchLogServiceImpl implements IOrderDispatchLogService {

    private final OrderDispatchLogMapper orderDispatchLogMapper;

    public OrderDispatchLogServiceImpl(OrderDispatchLogMapper orderDispatchLogMapper) {
        this.orderDispatchLogMapper = orderDispatchLogMapper;
    }

    @Override
    public int insertOrderDispatchLog(OrderDispatchLog log) {
        return orderDispatchLogMapper.insertOrderDispatchLog(log);
    }

    @Override
    public List<OrderDispatchLog> selectOrderDispatchLogList(OrderDispatchLog query) {
        return orderDispatchLogMapper.selectOrderDispatchLogList(query);
    }

    @Override
    public List<OrderDispatchLog> selectLogsByOrderId(Long orderId) {
        return orderDispatchLogMapper.selectLogsByOrderId(orderId);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        return orderDispatchLogMapper.updateStatus(id, status);
    }

    @Override
    public int updateStatusByOrderRound(Long orderId, Integer round, Integer status) {
        return orderDispatchLogMapper.updateStatusByOrderRound(orderId, round, status);
    }

    @Override
    public int markOthersAccepted(Long orderId, Long runnerUserId) {
        return orderDispatchLogMapper.markOthersAccepted(orderId, runnerUserId);
    }

    @Override
    public int markAccepted(Long orderId, Long runnerUserId) {
        return orderDispatchLogMapper.markAccepted(orderId, runnerUserId);
    }

    @Override
    public int countRounds(Long orderId) {
        return orderDispatchLogMapper.countRounds(orderId);
    }

    @Override
    public int markNoticeRead(Long runnerUserId, List<Long> logIds) {
        return orderDispatchLogMapper.markNoticeRead(runnerUserId, logIds);
    }

    @Override
    public List<Long> selectPendingTakeoutOrderIds() {
        return orderDispatchLogMapper.selectPendingTakeoutOrderIds();
    }
}
