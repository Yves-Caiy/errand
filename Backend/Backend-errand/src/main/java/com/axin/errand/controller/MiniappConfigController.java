package com.axin.errand.controller;

import com.alibaba.fastjson2.JSON;
import com.axin.common.annotation.Anonymous;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.system.domain.MiniappConfig;
import com.axin.system.service.IMiniappConfigService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;

@Validated
@Tag(name = "小程序配置接口")
@ApiSupport(author = "AXIN", order = 2)
@RestController("miniappApiConfigController")
@RequestMapping("/miniapp/config")
public class MiniappConfigController extends BaseController {

    @Autowired
    private IMiniappConfigService miniappConfigService;

    @Operation(summary = "获取首页轮播图配置")
    @ApiOperationSupport(order = 40)
    @Anonymous
    @GetMapping("/carousel")
    public AjaxResult carouselConfig() {
        MiniappConfig cfg = miniappConfigService.getCurrent();
        java.util.List<CarouselItem> list = new java.util.ArrayList<>();
        if (cfg != null && cfg.getCarouselImages() != null && !cfg.getCarouselImages().isEmpty()) {
            try {
                list = JSON.parseArray(cfg.getCarouselImages(), CarouselItem.class);
            } catch (Exception e) {
                list = new java.util.ArrayList<>();
            }
        }
        list.sort(Comparator.comparingInt(o -> o.getSortOrder() == null ? 0 : o.getSortOrder()));
        return success(list);
    }

    @Operation(summary = "获取隐私政策")
    @ApiOperationSupport(order = 45)
    @Anonymous
    @GetMapping("/privacyPolicy")
    public AjaxResult privacyPolicy() {
        MiniappConfig cfg = miniappConfigService.getCurrent();
        String policy = cfg == null ? "" : (cfg.getPrivacyPolicy() == null ? "" : cfg.getPrivacyPolicy());
        return AjaxResult.success("操作成功", policy);
    }

    @Operation(summary = "获取公告通知")
    @ApiOperationSupport(order = 46)
    @Anonymous
    @GetMapping("/notice")
    public AjaxResult noticeConfig() {
        MiniappConfig cfg = miniappConfigService.getCurrent();
        String notice = cfg == null ? "" : (cfg.getNoticeContent() == null ? "" : cfg.getNoticeContent());
        return AjaxResult.success("操作成功", notice);
    }

    @Operation(summary = "获取提现比率")
    @ApiOperationSupport(order = 50)
    @Anonymous
    @GetMapping("/withdrawRate")
    public AjaxResult withdrawRate() {
        MiniappConfig cfg = miniappConfigService.getCurrent();
        Integer rate = cfg == null ? 0 : (cfg.getWithdrawRate() == null ? 0 : cfg.getWithdrawRate());
        return success(rate);
    }

    public static class CarouselItem {
        private String imageUrl;
        private String linkUrl;
        private Integer sortOrder;

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
        public String getLinkUrl() { return linkUrl; }
        public void setLinkUrl(String linkUrl) { this.linkUrl = linkUrl; }
        public Integer getSortOrder() { return sortOrder; }
        public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }
    }
}
