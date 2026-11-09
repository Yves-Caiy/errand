package com.axin.errand.controller;

import com.axin.common.annotation.Anonymous;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.errand.domain.MiniLoginUser;
import com.axin.errand.domain.vo.MyServicePackageVO;
import com.axin.errand.service.MiniTokenService;
import com.axin.system.domain.OrderItem;
import com.axin.system.domain.OrderMain;
import com.axin.system.domain.ServicePackage;
import com.axin.system.domain.vo.ServicePackageVO;
import com.axin.system.service.IOrderService;
import com.axin.system.service.IServicePackageService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "小程序服务套餐接口")
@ApiSupport(author = "AXIN", order = 6)
@RestController
@RequestMapping("/miniapp/servicePackage")
public class MiniappServicePackageController extends BaseController {

    @Autowired
    private IServicePackageService servicePackageService;

    @Autowired
    private MiniTokenService tokenService;

    @Autowired
    private IOrderService orderService;



    @Operation(summary = "获取服务套餐列表")
    @ApiOperationSupport(order = 90)
    @Anonymous
    @GetMapping("/list")
    public AjaxResult servicePackageList(@RequestParam(value = "typeId", required = false) Long typeId) {
        ServicePackage query = new ServicePackage();
        query.setTypeId(typeId);
        query.setIsEnabled(1);
        java.util.List<ServicePackageVO> list = servicePackageService.selectServicePackageList(query).stream().map(this::toServicePackageVO).collect(java.util.stream.Collectors.toList());
        return success(list);
    }

    @Operation(summary = "获取我的服务套餐列表")
    @ApiOperationSupport(order = 95)
    @GetMapping("/myList")
    public AjaxResult myServicePackageList(HttpServletRequest request) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        OrderMain query = new OrderMain();
        query.setUserId(loginUser.getUserId());
        query.setOrderType(3); // 套餐
        // query.setPayStatus(1); // 已支付
        
        java.util.List<OrderMain> orders = orderService.selectOrderMainList(query);
        logger.info("myServicePackageList: found {} orders for user {}", orders.size(), loginUser.getUserId());

        java.util.List<MyServicePackageVO> list = new java.util.ArrayList<>();
        
        java.util.Date now = new java.util.Date();
        
        for (OrderMain order : orders) {
            java.util.List<OrderItem> items = orderService.selectOrderItemsByOrderId(order.getId());
            if (items == null || items.isEmpty()) {
                logger.info("myServicePackageList: order {} has no items", order.getId());
                continue;
            }
            
            for (OrderItem item : items) {
                logger.info("myServicePackageList: processing item {}, productId={}", item.getId(), item.getProductId());
                if (item.getProductId() != null) {
                    MyServicePackageVO vo = new MyServicePackageVO();
                    vo.setOrderId(order.getId());
                    vo.setPackageId(item.getProductId());
                    vo.setPackageName(item.getProductName());
                    vo.setBuyTime(order.getPayTime());
                    
                    ServicePackage sp = servicePackageService.selectServicePackageById(item.getProductId());
                    if (sp != null) {
                         vo.setIncludeService(sp.getIncludeService());
                         if (sp.getDurationDays() != null && order.getPayTime() != null) {
                             long expire = order.getPayTime().getTime() + (long)sp.getDurationDays() * 24 * 3600 * 1000;
                             vo.setExpireTime(new java.util.Date(expire));
                             vo.setStatus(now.getTime() < expire ? 1 : 0);
                         } else {
                             vo.setStatus(0);
                         }
                    } else {
                         logger.warn("myServicePackageList: ServicePackage {} not found for item {}", item.getProductId(), item.getId());
                         vo.setStatus(0);
                    }
                    list.add(vo);
                } else {
                    logger.warn("myServicePackageList: item {} skipped because productId is null", item.getId());
                }
            }
        }
        list.sort((a, b) -> {
            if (a.getBuyTime() == null) return 1;
            if (b.getBuyTime() == null) return -1;
            return b.getBuyTime().compareTo(a.getBuyTime());
        });
        
        return success(list);
    }

    private ServicePackageVO toServicePackageVO(ServicePackage e) {
        if (e == null) return null;
        ServicePackageVO vo = new ServicePackageVO();
        vo.setId(e.getId());
        vo.setTypeId(e.getTypeId());
        vo.setPackageName(e.getPackageName());
        vo.setPrice(e.getPrice());
        vo.setFirstMonthPrice(e.getFirstMonthPrice());
        vo.setDurationType(e.getDurationType());
        vo.setDurationDays(e.getDurationDays());
        vo.setIncludeService(e.getIncludeService());
        vo.setIsEnabled(e.getIsEnabled());
        vo.setCreateTime(e.getCreateTime());
        vo.setUpdateTime(e.getUpdateTime());
        return vo;
    }

    @Operation(summary = "检查当前服务类型是否有有效套餐")
    @ApiOperationSupport(order = 100)
    @GetMapping("/checkValid")
    public AjaxResult checkValidPackage(HttpServletRequest request, @RequestParam("typeId") Long typeId) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }

        // 1. 查询该服务类型下的所有启用套餐
        ServicePackage queryPkg = new ServicePackage();
        queryPkg.setTypeId(typeId);
        queryPkg.setIsEnabled(1);
        java.util.List<ServicePackage> packages = servicePackageService.selectServicePackageList(queryPkg);
        
        if (packages == null || packages.isEmpty()) {
            return success(null); // 该类型下没有配置套餐
        }
        
        // 收集套餐ID
        java.util.Map<Long, ServicePackage> pkgMap = packages.stream()
            .collect(java.util.stream.Collectors.toMap(ServicePackage::getId, java.util.function.Function.identity()));

        // 2. 查询用户已支付的套餐订单
        OrderMain queryOrder = new OrderMain();
        queryOrder.setUserId(loginUser.getUserId());
        queryOrder.setOrderType(3); // 套餐
        queryOrder.setPayStatus(1); // 已支付
        
        java.util.List<OrderMain> orders = orderService.selectOrderMainList(queryOrder);
        if (orders == null || orders.isEmpty()) {
            return success(null);
        }
        
        java.util.Date now = new java.util.Date();
        
        for (OrderMain order : orders) {
            java.util.List<OrderItem> items = orderService.selectOrderItemsByOrderId(order.getId());
            if (items == null) continue;
            
            for (OrderItem item : items) {
                // 使用 productId (根据之前的逻辑修正)
                Long pkgId = item.getProductId();
                if (pkgId != null && pkgMap.containsKey(pkgId)) {
                    ServicePackage sp = pkgMap.get(pkgId);
                    
                    // 计算过期时间
                    if (sp.getDurationDays() != null && order.getPayTime() != null) {
                        long expire = order.getPayTime().getTime() + (long)sp.getDurationDays() * 24 * 3600 * 1000;
                        if (now.getTime() < expire) {
                            // 找到有效套餐
                            MyServicePackageVO vo = new MyServicePackageVO();
                            vo.setOrderId(order.getId());
                            vo.setPackageId(pkgId);
                            vo.setPackageName(sp.getPackageName());
                            vo.setIncludeService(sp.getIncludeService());
                            vo.setBuyTime(order.getPayTime());
                            vo.setExpireTime(new java.util.Date(expire));
                            vo.setStatus(1);
                            return success(vo);
                        }
                    }
                }
            }
        }
        
        return success(null);
    }

}
