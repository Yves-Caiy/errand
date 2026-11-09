package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.ServicePackage;
import com.axin.system.domain.dto.ServicePackageCreateDTO;
import com.axin.system.domain.dto.ServicePackageQueryDTO;
import com.axin.system.domain.dto.ServicePackageUpdateDTO;
import com.axin.system.domain.vo.ServicePackageVO;
import com.axin.system.service.IServicePackageService;
import com.axin.system.service.IOrderService;
import com.axin.system.domain.OrderMain;
import com.axin.system.domain.OrderItem;
import com.axin.system.service.IWechatUserService;
import com.axin.system.domain.WechatUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@Tag(name = "后台服务套餐管理", description = "服务套餐增删改查接口")
@RestController
@RequestMapping("/errand/servicePackage")
public class ServicePackageController extends BaseController {

    @Autowired
    private IServicePackageService service;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IWechatUserService wechatUserService;

    @Operation(summary = "分页查询套餐列表", description = "按类型、名称、时长类型、启用状态过滤")
    @PreAuthorize("@ss.hasPermi('errand:servicePackage:list')")
    @GetMapping("/list")
    public TableDataInfo list(ServicePackageQueryDTO queryDTO) {
        startPage();
        ServicePackage query = new ServicePackage();
        query.setTypeId(queryDTO.getTypeId());
        query.setPackageName(queryDTO.getPackageName());
        query.setDurationType(queryDTO.getDurationType());
        query.setIsEnabled(queryDTO.getIsEnabled());
        List<ServicePackage> list = service.selectServicePackageList(query);

        TableDataInfo data = getDataTable(list);
        List<ServicePackageVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "查询套餐详情")
    @PreAuthorize("@ss.hasPermi('errand:servicePackage:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        ServicePackage entity = service.selectServicePackageById(id);
        return success(toVO(entity));
    }

    @Operation(summary = "新增套餐")
    @PreAuthorize("@ss.hasPermi('errand:servicePackage:add')")
    @Log(title = "服务套餐管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody ServicePackageCreateDTO createDTO) {
        if (service.isPackageNameExists(createDTO.getTypeId(), createDTO.getPackageName(), null)) {
            return error("新增失败：同一服务类型下套餐名称已存在");
        }
        ServicePackage entity = new ServicePackage();
        entity.setTypeId(createDTO.getTypeId());
        entity.setPackageName(createDTO.getPackageName());
        entity.setPrice(createDTO.getPrice());
        entity.setFirstMonthPrice(createDTO.getFirstMonthPrice());
        entity.setDurationType(createDTO.getDurationType());
        entity.setDurationDays(createDTO.getDurationDays());
        entity.setIncludeService(createDTO.getIncludeService());
        entity.setIsEnabled(createDTO.getIsEnabled());
        return toAjax(service.insertServicePackage(entity));
    }

    @Operation(summary = "修改套餐")
    @PreAuthorize("@ss.hasPermi('errand:servicePackage:edit')")
    @Log(title = "服务套餐管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody ServicePackageUpdateDTO updateDTO) {
        if (updateDTO.getTypeId() != null && updateDTO.getPackageName() != null
                && service.isPackageNameExists(updateDTO.getTypeId(), updateDTO.getPackageName(), updateDTO.getId())) {
            return error("修改失败：同一服务类型下套餐名称已存在");
        }
        ServicePackage entity = new ServicePackage();
        entity.setId(updateDTO.getId());
        entity.setTypeId(updateDTO.getTypeId());
        entity.setPackageName(updateDTO.getPackageName());
        entity.setPrice(updateDTO.getPrice());
        entity.setFirstMonthPrice(updateDTO.getFirstMonthPrice());
        entity.setDurationType(updateDTO.getDurationType());
        entity.setDurationDays(updateDTO.getDurationDays());
        entity.setIncludeService(updateDTO.getIncludeService());
        entity.setIsEnabled(updateDTO.getIsEnabled());
        return toAjax(service.updateServicePackage(entity));
    }

    @Operation(summary = "删除套餐")
    @PreAuthorize("@ss.hasPermi('errand:servicePackage:remove')")
    @Log(title = "服务套餐管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "待删除ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(service.deleteServicePackageByIds(ids));
    }

    @Operation(summary = "查询用户已购套餐列表")
    @PreAuthorize("@ss.hasPermi('errand:servicePackage:list')")
    @GetMapping("/user/{userId}")
    public AjaxResult getUserPackageList(@Parameter(description = "用户ID") @PathVariable @NotNull Long userId) {
        OrderMain query = new OrderMain();
        query.setUserId(userId);
        query.setOrderType(3); // 套餐
        query.setPayStatus(1); // 已支付
        
        List<OrderMain> orders = orderService.selectOrderMainList(query);
        List<java.util.Map<String, Object>> list = new java.util.ArrayList<>();
        
        java.util.Date now = new java.util.Date();
        
        for (OrderMain order : orders) {
            List<OrderItem> items = orderService.selectOrderItemsByOrderId(order.getId());
            if (items == null) continue;
            
            for (OrderItem item : items) {
                Long pkgId = item.getProductId();
                if (pkgId != null) {
                    ServicePackage sp = service.selectServicePackageById(pkgId);
                    if (sp != null) {
                         java.util.Map<String, Object> vo = new java.util.HashMap<>();
                         vo.put("orderId", order.getId());
                         vo.put("packageId", pkgId);
                         vo.put("packageName", item.getProductName());
                         vo.put("buyTime", order.getPayTime());
                         vo.put("includeService", sp.getIncludeService());
                         
                         if (sp.getDurationDays() != null && order.getPayTime() != null) {
                             long expire = order.getPayTime().getTime() + (long)sp.getDurationDays() * 24 * 3600 * 1000;
                             vo.put("expireTime", new java.util.Date(expire));
                             vo.put("status", now.getTime() < expire ? 1 : 0); // 1生效 0过期
                         } else {
                             vo.put("status", 0);
                         }
                         list.add(vo);
                    }
                }
            }
        }
        return success(list);
    }

    @Operation(summary = "查询有效期内套餐用户列表")
    @PreAuthorize("@ss.hasPermi('errand:servicePackage:list')")
    @GetMapping("/validUsers")
    public AjaxResult listValidUsersPackages() {
        OrderMain query = new OrderMain();
        query.setOrderType(3);
        query.setPayStatus(1);
        List<OrderMain> orders = orderService.selectOrderMainList(query);
        java.util.Date now = new java.util.Date();
        java.util.Map<Long, java.util.Map<String, Object>> agg = new java.util.LinkedHashMap<>();
        for (OrderMain order : orders) {
            if (order.getPayTime() == null) continue;
            List<OrderItem> items = orderService.selectOrderItemsByOrderId(order.getId());
            if (items == null || items.isEmpty()) continue;
            for (OrderItem item : items) {
                Integer t = item.getItemType();
                if (t == null || t != 3) continue;
                Long pkgId = item.getProductId() != null ? item.getProductId() : item.getPackageId();
                if (pkgId == null) continue;
                ServicePackage sp = service.selectServicePackageById(pkgId);
                if (sp == null || sp.getDurationDays() == null) continue;
                long expire = order.getPayTime().getTime() + (long) sp.getDurationDays() * 24 * 3600 * 1000;
                if (now.getTime() >= expire) continue;
                Long userId = order.getUserId();
                if (!agg.containsKey(userId)) {
                    WechatUser u = wechatUserService.selectWechatUserById(userId);
                    java.util.Map<String, Object> userEntry = new java.util.HashMap<>();
                    java.util.Map<String, Object> userInfo = new java.util.HashMap<>();
                    if (u != null) {
                        userInfo.put("id", u.getId());
                        userInfo.put("nickname", u.getNickname());
                        userInfo.put("phone", u.getPhone());
                        userInfo.put("avatarUrl", u.getAvatarUrl());
                    } else {
                        userInfo.put("id", userId);
                    }
                    userEntry.put("user", userInfo);
                    userEntry.put("packages", new java.util.ArrayList<java.util.Map<String, Object>>());
                    agg.put(userId, userEntry);
                }
                java.util.Map<String, Object> pkgInfo = new java.util.HashMap<>();
                pkgInfo.put("orderId", order.getId());
                pkgInfo.put("packageId", pkgId);
                pkgInfo.put("packageName", sp.getPackageName());
                pkgInfo.put("durationDays", sp.getDurationDays());
                pkgInfo.put("buyTime", order.getPayTime());
                pkgInfo.put("expireTime", new java.util.Date(expire));
                ((java.util.List<java.util.Map<String, Object>>) agg.get(order.getUserId()).get("packages")).add(pkgInfo);
            }
        }
        java.util.List<java.util.Map<String, Object>> result = new java.util.ArrayList<>(agg.values());
        return success(result);
    }

    private ServicePackageVO toVO(ServicePackage e) {
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
}
