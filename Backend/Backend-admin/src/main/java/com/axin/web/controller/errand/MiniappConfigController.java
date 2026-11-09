package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.MiniappConfig;
import com.axin.system.domain.dto.MiniappConfigUpdateDTO;
import com.axin.system.domain.vo.MiniappConfigVO;
import com.axin.system.service.IMiniappConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "后台小程序配置管理", description = "提现比率、首页轮播图、隐私政策设置")
@RestController
@RequestMapping("/errand/miniappConfig")
public class MiniappConfigController extends BaseController {

    @Autowired
    private IMiniappConfigService configService;

    @Operation(summary = "查询当前小程序配置")
    @PreAuthorize("@ss.hasPermi('errand:miniappConfig:query')")
    @GetMapping("/current")
    public AjaxResult getCurrent() {
        MiniappConfig cfg = configService.getCurrent();
        return success(toVO(cfg));
    }

    @Operation(summary = "更新当前小程序配置")
    @PreAuthorize("@ss.hasPermi('errand:miniappConfig:edit')")
    @Log(title = "小程序配置", businessType = BusinessType.UPDATE)
    @PutMapping("/current")
    public AjaxResult update(@Valid @RequestBody MiniappConfigUpdateDTO dto) {
        MiniappConfig cfg = new MiniappConfig();
        cfg.setWithdrawRate(dto.getWithdrawRate());
        cfg.setCarouselImages(dto.getCarouselImages());
        cfg.setPrivacyPolicy(dto.getPrivacyPolicy());
        cfg.setNoticeContent(dto.getNoticeContent());
        cfg.setIsEnabled(dto.getIsEnabled());
        cfg.setTestMode(dto.getTestMode());
        cfg.setDispatchEnabled(dto.getDispatchEnabled());
        cfg.setDispatchOrderTypes(dto.getDispatchOrderTypes());
        cfg.setDispatchPushCount(dto.getDispatchPushCount());
        cfg.setDispatchTimeoutSec(dto.getDispatchTimeoutSec());
        cfg.setDispatchMaxRound(dto.getDispatchMaxRound());
        cfg.setDispatchWeightJson(dto.getDispatchWeightJson());
        configService.saveOrUpdate(cfg);
        return success(true);
    }

    private MiniappConfigVO toVO(MiniappConfig c) {
        if (c == null) return null;
        MiniappConfigVO vo = new MiniappConfigVO();
        vo.setId(c.getId());
        vo.setWithdrawRate(c.getWithdrawRate());
        vo.setCarouselImages(c.getCarouselImages());
        vo.setPrivacyPolicy(c.getPrivacyPolicy());
        vo.setNoticeContent(c.getNoticeContent());
        vo.setIsEnabled(c.getIsEnabled());
        vo.setTestMode(c.getTestMode());
        vo.setDispatchEnabled(c.getDispatchEnabled());
        vo.setDispatchOrderTypes(c.getDispatchOrderTypes());
        vo.setDispatchPushCount(c.getDispatchPushCount());
        vo.setDispatchTimeoutSec(c.getDispatchTimeoutSec());
        vo.setDispatchMaxRound(c.getDispatchMaxRound());
        vo.setDispatchWeightJson(c.getDispatchWeightJson());
        vo.setCreateTime(c.getCreateTime());
        vo.setUpdateTime(c.getUpdateTime());
        return vo;
    }
}

