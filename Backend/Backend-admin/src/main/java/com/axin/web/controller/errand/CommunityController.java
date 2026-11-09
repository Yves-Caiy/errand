package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.Community;
import com.axin.system.domain.dto.CommunityCreateDTO;
import com.axin.system.domain.dto.CommunityQueryDTO;
import com.axin.system.domain.dto.CommunityUpdateDTO;
import com.axin.system.domain.vo.CommunityVO;
import com.axin.system.service.ICommunityService;
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
@Tag(name = "后台小区管理", description = "后台小区管理接口")
@RestController
@RequestMapping("/errand/community")
public class CommunityController extends BaseController {

    @Autowired
    private ICommunityService communityService;

    @Operation(summary = "分页查询小区列表", description = "按名称、地址、启用状态过滤")
    @PreAuthorize("@ss.hasPermi('errand:community:list')")
    @GetMapping("/list")
    public TableDataInfo list(CommunityQueryDTO queryDTO) {
        startPage();
        Community query = new Community();
        query.setCommunityName(queryDTO.getCommunityName());
        query.setAddress(queryDTO.getAddress());
        query.setIsEnabled(queryDTO.getIsEnabled());
        List<Community> list = communityService.selectCommunityList(query);

        // 先用原始列表生成分页信息，再替换为VO列表，避免丢失分页元数据
        TableDataInfo data = getDataTable(list);
        List<CommunityVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "查询小区详情")
    @PreAuthorize("@ss.hasPermi('errand:community:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        Community community = communityService.selectCommunityById(id);
        return success(toVO(community));
    }

    @Operation(summary = "新增小区")
    @PreAuthorize("@ss.hasPermi('errand:community:add')")
    @Log(title = "小区管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody CommunityCreateDTO createDTO) {
        if (communityService.isCommunityNameExists(createDTO.getCommunityName(), null)) {
            return error("新增小区失败，小区名称已存在");
        }
        Community community = new Community();
        community.setCommunityName(createDTO.getCommunityName());
        community.setAddress(createDTO.getAddress());
        community.setContactPerson(createDTO.getContactPerson());
        community.setContactPhone(createDTO.getContactPhone());
        community.setIsEnabled(createDTO.getIsEnabled());
        return toAjax(communityService.insertCommunity(community));
    }

    @Operation(summary = "修改小区")
    @PreAuthorize("@ss.hasPermi('errand:community:edit')")
    @Log(title = "小区管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody CommunityUpdateDTO updateDTO) {
        if (updateDTO.getCommunityName() != null
                && communityService.isCommunityNameExists(updateDTO.getCommunityName(), updateDTO.getId())) {
            return error("修改小区失败，小区名称已存在");
        }
        Community community = new Community();
        community.setId(updateDTO.getId());
        community.setCommunityName(updateDTO.getCommunityName());
        community.setAddress(updateDTO.getAddress());
        community.setContactPerson(updateDTO.getContactPerson());
        community.setContactPhone(updateDTO.getContactPhone());
        community.setIsEnabled(updateDTO.getIsEnabled());
        return toAjax(communityService.updateCommunity(community));
    }

    @Operation(summary = "删除小区")
    @PreAuthorize("@ss.hasPermi('errand:community:remove')")
    @Log(title = "小区管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "待删除ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(communityService.deleteCommunityByIds(ids));
    }

    private CommunityVO toVO(Community c) {
        if (c == null) return null;
        CommunityVO vo = new CommunityVO();
        vo.setId(c.getId());
        vo.setCommunityName(c.getCommunityName());
        vo.setAddress(c.getAddress());
        vo.setContactPerson(c.getContactPerson());
        vo.setContactPhone(c.getContactPhone());
        vo.setIsEnabled(c.getIsEnabled());
        vo.setCreateTime(c.getCreateTime());
        vo.setUpdateTime(c.getUpdateTime());
        return vo;
    }
}
