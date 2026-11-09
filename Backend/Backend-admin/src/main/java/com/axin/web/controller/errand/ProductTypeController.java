package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.ProductType;
import com.axin.system.domain.dto.ProductTypeCreateDTO;
import com.axin.system.domain.dto.ProductTypeQueryDTO;
import com.axin.system.domain.dto.ProductTypeUpdateDTO;
import com.axin.system.domain.vo.ProductTypeVO;
import com.axin.system.service.IProductTypeService;
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
@Tag(name = "后台商品（服务）类型管理", description = "后台商品（服务）类型管理接口")
@RestController
@RequestMapping("/errand/productType")
public class ProductTypeController extends BaseController {

    @Autowired
    private IProductTypeService productTypeService;

    @Operation(summary = "分页查询商品（服务）类型列表", description = "按名称、启用状态、可管理状态过滤")
    @PreAuthorize("@ss.hasPermi('errand:productType:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductTypeQueryDTO queryDTO) {
        startPage();
        ProductType query = new ProductType();
        query.setTypeName(queryDTO.getTypeName());
        query.setIsManageable(queryDTO.getIsManageable());
        query.setIsEnabled(queryDTO.getIsEnabled());
        List<ProductType> list = productTypeService.selectProductTypeList(query);

        TableDataInfo data = getDataTable(list);
        List<ProductTypeVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "查询商品（服务）类型详情")
    @PreAuthorize("@ss.hasPermi('errand:productType:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        ProductType entity = productTypeService.selectProductTypeById(id);
        return success(toVO(entity));
    }

    @Operation(summary = "新增商品（服务）类型")
    @PreAuthorize("@ss.hasPermi('errand:productType:add')")
    @Log(title = "商品（服务）类型管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody ProductTypeCreateDTO createDTO) {
        if (productTypeService.isTypeNameExists(createDTO.getTypeName(), null)) {
            return error("新增商品（服务）类型失败，模块名称已存在");
        }
        ProductType entity = new ProductType();
        entity.setTypeName(createDTO.getTypeName());
        entity.setTypeDesc(createDTO.getTypeDesc());
        entity.setIconUrl(createDTO.getIconUrl());
        entity.setIsManageable(createDTO.getIsManageable());
        entity.setIsEnabled(createDTO.getIsEnabled());
        return toAjax(productTypeService.insertProductType(entity));
    }

    @Operation(summary = "修改商品（服务）类型")
    @PreAuthorize("@ss.hasPermi('errand:productType:edit')")
    @Log(title = "商品（服务）类型管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody ProductTypeUpdateDTO updateDTO) {
        if (updateDTO.getTypeName() != null
                && productTypeService.isTypeNameExists(updateDTO.getTypeName(), updateDTO.getId())) {
            return error("修改商品（服务）类型失败，模块名称已存在");
        }
        ProductType entity = new ProductType();
        entity.setId(updateDTO.getId());
        entity.setTypeName(updateDTO.getTypeName());
        entity.setTypeDesc(updateDTO.getTypeDesc());
        entity.setIconUrl(updateDTO.getIconUrl());
        entity.setIsManageable(updateDTO.getIsManageable());
        entity.setIsEnabled(updateDTO.getIsEnabled());
        return toAjax(productTypeService.updateProductType(entity));
    }

    @Operation(summary = "删除商品（服务）类型")
    @PreAuthorize("@ss.hasPermi('errand:productType:remove')")
    @Log(title = "商品（服务）类型管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "待删除ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(productTypeService.deleteProductTypeByIds(ids));
    }

    private ProductTypeVO toVO(ProductType p) {
        if (p == null) return null;
        ProductTypeVO vo = new ProductTypeVO();
        vo.setId(p.getId());
        vo.setTypeName(p.getTypeName());
        vo.setTypeDesc(p.getTypeDesc());
        vo.setIconUrl(p.getIconUrl());
        vo.setIsManageable(p.getIsManageable());
        vo.setIsEnabled(p.getIsEnabled());
        vo.setCreateTime(p.getCreateTime());
        vo.setUpdateTime(p.getUpdateTime());
        return vo;
    }
}

