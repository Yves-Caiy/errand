package com.axin.errand.controller;

import com.axin.common.annotation.Anonymous;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.system.domain.TakeawayProduct;
import com.axin.system.domain.TakeawayStore;
import com.axin.system.domain.vo.TakeawayProductVO;
import com.axin.system.domain.vo.TakeawayStoreVO;
import com.axin.system.service.ITakeawayProductService;
import com.axin.system.service.ITakeawayStoreService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "小程序外卖接口")
@ApiSupport(author = "AXIN", order = 5)
@RestController
@RequestMapping("/miniapp")
public class MiniappTakeawayController extends BaseController {

    @Autowired
    private ITakeawayStoreService takeawayStoreService;

    @Autowired
    private ITakeawayProductService takeawayProductService;

    @Operation(summary = "获取外卖店铺列表")
    @ApiOperationSupport(order = 100)
    @Anonymous
    @GetMapping("/takeawayStore/list")
    public AjaxResult takeawayStoreList(@RequestParam(value = "communityId", required = false) Long communityId) {
        TakeawayStore query = new TakeawayStore();
        query.setCommunityId(communityId);
        query.setIsEnabled(1);
        java.util.List<TakeawayStoreVO> list = takeawayStoreService.selectTakeawayStoreList(query)
                .stream().map(this::toTakeawayStoreVO).collect(java.util.stream.Collectors.toList());
        return success(list);
    }

    @Operation(summary = "根据店铺获取外卖商品列表")
    @ApiOperationSupport(order = 110)
    @Anonymous
    @GetMapping("/takeawayProduct/list")
    public AjaxResult takeawayProductList(@RequestParam("storeId") Long storeId) {
        TakeawayProduct query = new TakeawayProduct();
        query.setStoreId(storeId);
        query.setIsEnabled(1);
        java.util.List<TakeawayProductVO> list = takeawayProductService.selectTakeawayProductList(query)
                .stream().map(this::toTakeawayProductVO).collect(java.util.stream.Collectors.toList());
        return success(list);
    }

    private TakeawayStoreVO toTakeawayStoreVO(TakeawayStore s) {
        if (s == null) return null;
        TakeawayStoreVO vo = new TakeawayStoreVO();
        vo.setId(s.getId());
        vo.setCommunityId(s.getCommunityId());
        vo.setStoreName(s.getStoreName());
        vo.setAvatarUrl(s.getAvatarUrl());
        vo.setAddress(s.getAddress());
        vo.setContact(s.getContact());
        vo.setIsEnabled(s.getIsEnabled());
        vo.setCreateTime(s.getCreateTime());
        vo.setUpdateTime(s.getUpdateTime());
        return vo;
    }

    private TakeawayProductVO toTakeawayProductVO(TakeawayProduct p) {
        if (p == null) return null;
        TakeawayProductVO vo = new TakeawayProductVO();
        vo.setId(p.getId());
        vo.setStoreId(p.getStoreId());
        vo.setProductName(p.getProductName());
        vo.setPrice(p.getPrice());
        vo.setImageUrl(p.getImageUrl());
        vo.setProductType(p.getProductType());
        vo.setIsEnabled(p.getIsEnabled());
        vo.setCreateTime(p.getCreateTime());
        vo.setUpdateTime(p.getUpdateTime());
        return vo;
    }
}
