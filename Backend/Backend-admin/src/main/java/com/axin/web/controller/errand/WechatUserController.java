package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.WechatUser;
import com.axin.system.domain.dto.WechatUserQueryDTO;
import com.axin.system.domain.vo.WechatUserVO;
import com.axin.system.domain.Community;
import com.axin.system.service.ICommunityService;
import com.axin.system.service.IWechatUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@Tag(name = "后台微信用户管理", description = "仅查询与账号状态管理接口")
@RestController
@RequestMapping("/errand/wechatUser")
public class WechatUserController extends BaseController {

    @Autowired
    private IWechatUserService wechatUserService;
    @Autowired
    private ICommunityService communityService;

    @Operation(summary = "分页查询用户列表", description = "按openid、昵称、手机号、启用状态过滤")
    @PreAuthorize("@ss.hasPermi('errand:wechatUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(WechatUserQueryDTO queryDTO) {
        startPage();
        WechatUser query = new WechatUser();
        query.setOpenid(queryDTO.getOpenid());
        query.setNickname(queryDTO.getNickname());
        query.setPhone(queryDTO.getPhone());
        query.setCommunityId(queryDTO.getCommunityId());
        query.setIsEnabled(queryDTO.getIsEnabled());
        List<WechatUser> list = wechatUserService.selectWechatUserList(query);

        TableDataInfo data = getDataTable(list);
        List<WechatUserVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "查询用户详情")
    @PreAuthorize("@ss.hasPermi('errand:wechatUser:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        WechatUser entity = wechatUserService.selectWechatUserById(id);
        return success(toVO(entity));
    }

    @Operation(summary = "批量修改账号启用状态")
    @PreAuthorize("@ss.hasPermi('errand:wechatUser:status')")
    @Log(title = "微信用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/status/{enabled}/{ids}")
    public AjaxResult changeStatus(@Parameter(description = "启用状态 1启用 0禁用") @PathVariable Integer enabled,
                                   @Parameter(description = "ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(wechatUserService.updateEnabledByIds(ids, enabled));
    }

    @Operation(summary = "批量设置管理员状态")
    @PreAuthorize("@ss.hasPermi('errand:wechatUser:admin')")
    @Log(title = "微信用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/admin/{isAdmin}/{ids}")
    public AjaxResult changeAdminStatus(@Parameter(description = "管理员状态 1是 0否") @PathVariable Integer isAdmin,
                                        @Parameter(description = "ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(wechatUserService.updateAdminStatusByIds(ids, isAdmin));
    }

    private WechatUserVO toVO(WechatUser u) {
        if (u == null) return null;
        WechatUserVO vo = new WechatUserVO();
        vo.setId(u.getId());
        vo.setOpenid(u.getOpenid());
        vo.setNickname(u.getNickname());
        vo.setAvatarUrl(u.getAvatarUrl());
        vo.setPhone(u.getPhone());
        if (u.getCommunityId() != null) {
            Community c = communityService.selectCommunityById(u.getCommunityId());
            if (c != null) {
                vo.setCommunityName(c.getCommunityName());
            }
        }
        vo.setDetailAddress(u.getDetailAddress());
        vo.setTotalCommission(u.getTotalCommission());
        vo.setPendingCommission(u.getPendingCommission());
        vo.setSettledCommission(u.getSettledCommission());
        vo.setIsEnabled(u.getIsEnabled());
        vo.setIsAdmin(u.getIsAdmin());
        vo.setCreateTime(u.getCreateTime());
        vo.setUpdateTime(u.getUpdateTime());
        return vo;
    }
}
