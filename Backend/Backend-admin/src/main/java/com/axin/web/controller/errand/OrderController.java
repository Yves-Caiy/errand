package com.axin.web.controller.errand;

import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.system.domain.OrderItem;
import com.axin.system.domain.OrderMain;
import com.axin.system.domain.dto.OrderQueryDTO;
import com.axin.system.domain.vo.OrderItemVO;
import com.axin.system.domain.vo.OrderVO;
import com.axin.system.domain.WechatUser;
import com.axin.system.domain.Community;
import com.axin.system.service.IOrderService;
import com.axin.system.service.IWechatUserService;
import com.axin.system.service.ICommunityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.HashMap;

@Validated
@Tag(name = "后台订单管理", description = "订单查询接口")
@RestController
@RequestMapping("/errand/order")
public class OrderController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private IOrderService orderService;
    @Autowired
    private IWechatUserService wechatUserService;
    @Autowired
    private ICommunityService communityService;

    @Operation(summary = "分页查询订单列表", description = "按订单号、用户、接单员、小区、类型、状态、支付状态筛选")
    @PreAuthorize("@ss.hasPermi('errand:order:list')")
    @GetMapping("/list")
    public TableDataInfo list(OrderQueryDTO queryDTO) {
        startPage();
        OrderMain query = new OrderMain();
        query.setOrderNo(queryDTO.getOrderNo());
        query.setUserId(queryDTO.getUserId());
        query.setRunnerId(queryDTO.getRunnerId());
        query.setCommunityId(queryDTO.getCommunityId());
        query.setOrderType(queryDTO.getOrderType());
        query.setOrderStatus(queryDTO.getOrderStatus());
        query.setPayStatus(queryDTO.getPayStatus());
        query.setServiceType(queryDTO.getServiceType());
        List<OrderMain> list = orderService.selectOrderMainList(query);

        TableDataInfo data = getDataTable(list);
        List<OrderVO> voList = list.stream().map(this::toVOWithoutItems).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "申请退款（模拟退款，直接标记已退款）")
    @PreAuthorize("@ss.hasPermi('errand:order:refund:apply')")
    @PostMapping("/refund/apply")
    public AjaxResult applyRefund(@RequestBody HashMap<String, Object> dto) {
        Object idObj = dto.get("id");
        Long id = idObj == null ? null : (idObj instanceof Number ? ((Number) idObj).longValue() : Long.valueOf(String.valueOf(idObj)));
        if (id == null) {
            return error("id不能为空");
        }
        OrderMain order = orderService.selectOrderMainById(id);
        if (order == null) {
            return error("订单不存在");
        }
        if (order.getPayStatus() == null || (order.getPayStatus() != 2 && order.getPayStatus() != 1)) {
            return error("当前订单不可退款");
        }
        long total = safeAmount(order);
        if (total <= 0) {
            return error("订单金额异常");
        }
        Object refundAmountObj = dto.get("refundAmount");
        long refundAmount = (refundAmountObj instanceof Number) ? ((Number) refundAmountObj).longValue() : 0L;
        if (refundAmount <= 0 || refundAmount > total) {
            refundAmount = total;
        }

        // 模拟退款：不走真实微信退款，直接标记订单为已退款
        OrderMain upd = new OrderMain();
        upd.setId(order.getId());
        upd.setOrderStatus(4);
        upd.setPayStatus(3);
        upd.setCancelReason("退款成功");
        orderService.updateOrderMain(upd);
        log.info("mock refund success, order id={}, orderNo={}, refundAmount={}", order.getId(), order.getOrderNo(), refundAmount);

        HashMap<String, Object> data = new HashMap<>();
        data.put("outRefundNo", order.getOrderNo() + "-RF-" + System.currentTimeMillis());
        data.put("refundAmount", refundAmount);
        data.put("mockRefund", true);
        return success(data);
    }

    @Operation(summary = "查询订单详情")
    @PreAuthorize("@ss.hasPermi('errand:order:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        OrderMain entity = orderService.selectOrderMainById(id);
        List<OrderItem> items = orderService.selectOrderItemsByOrderId(id);
        OrderVO vo = toVO(entity);
        vo.setItems(items.stream().map(this::toItemVO).collect(Collectors.toList()));
        return success(vo);
    }

    private OrderVO toVO(OrderMain e) {
        if (e == null) return null;
        OrderVO vo = new OrderVO();
        vo.setId(e.getId());
        vo.setOrderNo(e.getOrderNo());
        vo.setUserId(e.getUserId());
        vo.setRunnerId(e.getRunnerId());
        vo.setCommunityId(e.getCommunityId());
        vo.setDetailAddress(e.getDetailAddress());
        vo.setOrderType(e.getOrderType());
        vo.setServiceType(e.getServiceType());
        vo.setStartAddress(e.getStartAddress());
        vo.setEndAddress(e.getEndAddress());
        vo.setDeliveryAddress(e.getDeliveryAddress());
        vo.setItemDesc(e.getItemDesc());
        vo.setBudget(e.getBudget());
        vo.setSpecName(e.getSpecName());
        vo.setWechatId(e.getWechatId());
        vo.setDurationType(e.getDurationType());
        vo.setDurationValue(e.getDurationValue());
        vo.setTotalAmount(e.getTotalAmount());
        vo.setDiscountAmount(e.getDiscountAmount());
        vo.setActualAmount(e.getActualAmount());
        vo.setCommissionAmount(e.getCommissionAmount());
        vo.setOrderStatus(e.getOrderStatus());
        vo.setPayStatus(e.getPayStatus());
        vo.setPayMethod(e.getPayMethod());
        vo.setPayTime(e.getPayTime());
        vo.setFinishTime(e.getFinishTime());
        vo.setCancelReason(e.getCancelReason());
        vo.setNote(e.getNote());
        vo.setCreateTime(e.getCreateTime());
        vo.setUpdateTime(e.getUpdateTime());
        WechatUser u = e.getUserId() == null ? null : wechatUserService.selectWechatUserById(e.getUserId());
        if (u != null && u.getNickname() != null && !u.getNickname().isEmpty()) {
            vo.setUserNickname(u.getNickname());
        } else if (e.getUserId() != null) {
            vo.setUserNickname(String.valueOf(e.getUserId()));
        }
        if (e.getRunnerId() != null) {
            WechatUser r = wechatUserService.selectWechatUserById(e.getRunnerId());
            if (r != null && r.getNickname() != null && !r.getNickname().isEmpty()) {
                vo.setRunnerNickname(r.getNickname());
            } else {
                vo.setRunnerNickname(String.valueOf(e.getRunnerId()));
            }
        }
        Long cId = u != null && u.getCommunityId() != null ? u.getCommunityId() : e.getCommunityId();
        if (cId != null) {
            Community c = communityService.selectCommunityById(cId);
            if (c != null) {
                vo.setCommunityName(c.getCommunityName());
            }
        }
        return vo;
    }

    private OrderVO toVOWithoutItems(OrderMain e) {
        return toVO(e);
    }

    private OrderItemVO toItemVO(OrderItem item) {
        OrderItemVO vo = new OrderItemVO();
        vo.setId(item.getId());
        vo.setItemType(item.getItemType());
        vo.setPackageId(item.getPackageId());
        vo.setProductId(item.getProductId());
        vo.setProductName(item.getProductName());
        vo.setQuantity(item.getQuantity());
        vo.setUnitPrice(item.getUnitPrice());
        vo.setSubtotal(item.getSubtotal());
        return vo;
    }

    private static long safeAmount(OrderMain e) {
        Long a = e.getActualAmount();
        Long t = e.getTotalAmount();
        Long b = e.getBudget();
        if (a != null && a > 0) return a;
        if (t != null && t > 0) return t;
        if (b != null && b > 0) return b;
        return 0L;
    }
}
