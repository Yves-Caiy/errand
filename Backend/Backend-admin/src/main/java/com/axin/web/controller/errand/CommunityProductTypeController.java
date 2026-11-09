package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.CommunityProductType;
import com.axin.system.domain.dto.CommunityProductTypeCreateDTO;
import com.axin.system.domain.dto.CommunityProductTypeQueryDTO;
import com.axin.system.domain.dto.CommunityProductTypeUpdateDTO;
import com.axin.system.domain.vo.CommunityProductTypeVO;
import com.axin.system.service.ICommunityProductTypeService;
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
@Tag(name = "后台小区-商品类型关联管理", description = "后台小区-商品类型关联接口")
@RestController
@RequestMapping("/errand/communityProductType")
public class CommunityProductTypeController extends BaseController {

    @Autowired
    private ICommunityProductTypeService cptService;

    @Operation(summary = "分页查询关联列表", description = "按小区、类型、启用状态过滤")
    @PreAuthorize("@ss.hasPermi('errand:communityProductType:list')")
    @GetMapping("/list")
    public TableDataInfo list(CommunityProductTypeQueryDTO queryDTO) {
        startPage();
        CommunityProductType query = new CommunityProductType();
        query.setCommunityId(queryDTO.getCommunityId());
        query.setProductTypeId(queryDTO.getProductTypeId());
        query.setIsEnabled(queryDTO.getIsEnabled());
        List<CommunityProductType> list = cptService.selectCommunityProductTypeList(query);

        TableDataInfo data = getDataTable(list);
        List<CommunityProductTypeVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "查询关联详情")
    @PreAuthorize("@ss.hasPermi('errand:communityProductType:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        CommunityProductType entity = cptService.selectCommunityProductTypeById(id);
        return success(toVO(entity));
    }

    @Operation(summary = "新增关联")
    @PreAuthorize("@ss.hasPermi('errand:communityProductType:add')")
    @Log(title = "小区-商品类型关联", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody CommunityProductTypeCreateDTO createDTO) {
        if (cptService.isCommunityTypeExists(createDTO.getCommunityId(), createDTO.getProductTypeId(), null)) {
            return error("新增失败：该小区与该类型已关联");
        }
        CommunityProductType entity = new CommunityProductType();
        entity.setCommunityId(createDTO.getCommunityId());
        entity.setProductTypeId(createDTO.getProductTypeId());
        entity.setIsEnabled(createDTO.getIsEnabled());
        entity.setSortOrder(createDTO.getSortOrder());
        return toAjax(cptService.insertCommunityProductType(entity));
    }

    @Operation(summary = "修改关联")
    @PreAuthorize("@ss.hasPermi('errand:communityProductType:edit')")
    @Log(title = "小区-商品类型关联", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody CommunityProductTypeUpdateDTO updateDTO) {
        CommunityProductType current = cptService.selectCommunityProductTypeById(updateDTO.getId());
        Long targetCommunityId = updateDTO.getCommunityId() != null ? updateDTO.getCommunityId() : (current != null ? current.getCommunityId() : null);
        Long targetProductTypeId = updateDTO.getProductTypeId() != null ? updateDTO.getProductTypeId() : (current != null ? current.getProductTypeId() : null);
        if (targetCommunityId != null && targetProductTypeId != null
                && cptService.isCommunityTypeExists(targetCommunityId, targetProductTypeId, updateDTO.getId())) {
            return error("修改失败：该小区与该类型已关联");
        }
        CommunityProductType entity = new CommunityProductType();
        entity.setId(updateDTO.getId());
        entity.setCommunityId(updateDTO.getCommunityId());
        entity.setProductTypeId(updateDTO.getProductTypeId());
        entity.setIsEnabled(updateDTO.getIsEnabled());
        entity.setSortOrder(updateDTO.getSortOrder());
        return toAjax(cptService.updateCommunityProductType(entity));
    }

    @Operation(summary = "删除关联")
    @PreAuthorize("@ss.hasPermi('errand:communityProductType:remove')")
    @Log(title = "小区-商品类型关联", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "待删除ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(cptService.deleteCommunityProductTypeByIds(ids));
    }

    private CommunityProductTypeVO toVO(CommunityProductType e) {
        if (e == null) return null;
        CommunityProductTypeVO vo = new CommunityProductTypeVO();
        vo.setId(e.getId());
        vo.setCommunityId(e.getCommunityId());
        vo.setProductTypeId(e.getProductTypeId());
        vo.setIsEnabled(e.getIsEnabled());
        vo.setSortOrder(e.getSortOrder());
        vo.setCreateTime(e.getCreateTime());
        vo.setUpdateTime(e.getUpdateTime());
        return vo;
    }
}
