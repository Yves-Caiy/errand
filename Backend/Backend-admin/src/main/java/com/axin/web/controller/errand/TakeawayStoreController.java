package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.TakeawayStore;
import com.axin.system.domain.dto.TakeawayStoreCreateDTO;
import com.axin.system.domain.dto.TakeawayStoreQueryDTO;
import com.axin.system.domain.dto.TakeawayStoreUpdateDTO;
import com.axin.system.domain.vo.TakeawayStoreVO;
import com.axin.system.service.ITakeawayStoreService;
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
@Tag(name = "后台外卖店铺管理", description = "后台外卖店铺管理接口")
@RestController
@RequestMapping("/errand/takeawayStore")
public class TakeawayStoreController extends BaseController {

    @Autowired
    private ITakeawayStoreService storeService;

    @Operation(summary = "分页查询店铺列表", description = "按小区、名称、启用状态过滤")
    @PreAuthorize("@ss.hasPermi('errand:takeawayStore:list')")
    @GetMapping("/list")
    public TableDataInfo list(TakeawayStoreQueryDTO queryDTO) {
        startPage();
        TakeawayStore query = new TakeawayStore();
        query.setCommunityId(queryDTO.getCommunityId());
        query.setStoreName(queryDTO.getStoreName());
        query.setIsEnabled(queryDTO.getIsEnabled());
        List<TakeawayStore> list = storeService.selectTakeawayStoreList(query);

        TableDataInfo data = getDataTable(list);
        List<TakeawayStoreVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "查询店铺详情")
    @PreAuthorize("@ss.hasPermi('errand:takeawayStore:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        TakeawayStore entity = storeService.selectTakeawayStoreById(id);
        return success(toVO(entity));
    }

    @Operation(summary = "新增店铺")
    @PreAuthorize("@ss.hasPermi('errand:takeawayStore:add')")
    @Log(title = "外卖店铺管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody TakeawayStoreCreateDTO createDTO) {
        if (storeService.isStoreNameExists(createDTO.getStoreName(), null)) {
            return error("新增失败：店铺名称已存在");
        }
        TakeawayStore entity = new TakeawayStore();
        entity.setCommunityId(createDTO.getCommunityId());
        entity.setStoreName(createDTO.getStoreName());
        entity.setAvatarUrl(createDTO.getAvatarUrl());
        entity.setAddress(createDTO.getAddress());
        entity.setContact(createDTO.getContact());
        entity.setIsEnabled(createDTO.getIsEnabled());
        return toAjax(storeService.insertTakeawayStore(entity));
    }

    @Operation(summary = "修改店铺")
    @PreAuthorize("@ss.hasPermi('errand:takeawayStore:edit')")
    @Log(title = "外卖店铺管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody TakeawayStoreUpdateDTO updateDTO) {
        if (updateDTO.getStoreName() != null
                && storeService.isStoreNameExists(updateDTO.getStoreName(), updateDTO.getId())) {
            return error("修改失败：店铺名称已存在");
        }
        TakeawayStore entity = new TakeawayStore();
        entity.setId(updateDTO.getId());
        entity.setCommunityId(updateDTO.getCommunityId());
        entity.setStoreName(updateDTO.getStoreName());
        entity.setAvatarUrl(updateDTO.getAvatarUrl());
        entity.setAddress(updateDTO.getAddress());
        entity.setContact(updateDTO.getContact());
        entity.setIsEnabled(updateDTO.getIsEnabled());
        return toAjax(storeService.updateTakeawayStore(entity));
    }

    @Operation(summary = "删除店铺")
    @PreAuthorize("@ss.hasPermi('errand:takeawayStore:remove')")
    @Log(title = "外卖店铺管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "待删除ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(storeService.deleteTakeawayStoreByIds(ids));
    }

    private TakeawayStoreVO toVO(TakeawayStore s) {
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
}

