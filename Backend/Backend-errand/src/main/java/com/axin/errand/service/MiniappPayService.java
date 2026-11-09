package com.axin.errand.service;

import com.axin.common.core.domain.AjaxResult;
import com.axin.common.utils.StringUtils;
import com.axin.system.domain.OrderMain;
import com.axin.system.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MiniappPayService {

    private static final Logger logger = LoggerFactory.getLogger(MiniappPayService.class);

    @Autowired
    private IOrderService orderService;

    @Autowired
    private DispatchService dispatchService;

    /**
     * 模拟支付：不再调用真实微信支付，校验订单后直接标记为已支付
     */
    public AjaxResult prepay(String orderNo, Long userId) {
        if (StringUtils.isEmpty(orderNo)) {
            return AjaxResult.error("订单号不能为空");
        }
        if (userId == null) {
            return AjaxResult.error("用户不存在");
        }

        OrderMain query = new OrderMain();
        query.setOrderNo(orderNo);
        query.setUserId(userId);
        List<OrderMain> list = orderService.selectOrderMainList(query);
        OrderMain order = (list == null || list.isEmpty()) ? null : list.get(0);
        if (order == null) {
            return AjaxResult.error("订单不存在");
        }
        if (order.getPayStatus() != null && order.getPayStatus() == 1) {
            return AjaxResult.error("订单已支付");
        }

        long amount = 0L;
        if (order.getActualAmount() != null && order.getActualAmount() > 0L) {
            amount = order.getActualAmount();
        } else if (order.getTotalAmount() != null && order.getTotalAmount() > 0L) {
            amount = order.getTotalAmount();
        } else if (order.getBudget() != null && order.getBudget() > 0L) {
            amount = order.getBudget();
        }
        if (amount <= 0L) {
            return AjaxResult.error("订单金额异常");
        }

        OrderMain upd = new OrderMain();
        upd.setId(order.getId());
        upd.setPayStatus(1);
        upd.setPayTime(new Date());
        if (order.getOrderStatus() != null && order.getOrderStatus() == 0) {
            upd.setOrderStatus(1);
        }
        orderService.updateOrderMain(upd);
        logger.info("mock pay success, order id={} orderNo={} amount={}", order.getId(), orderNo, amount);

        // 支付成功触发智能派单（仅外卖 order_type=1，且开启派单、关闭测试模式时生效）
        dispatchService.triggerDispatch(order.getId());

        Map<String, Object> data = new HashMap<>();
        data.put("mockPay", true);
        data.put("orderNo", orderNo);
        data.put("amount", amount);
        return AjaxResult.success(data);
    }
}
