package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.TakeawayProduct;
import com.axin.system.domain.dto.TakeawayProductCreateDTO;
import com.axin.system.domain.dto.TakeawayProductQueryDTO;
import com.axin.system.domain.dto.TakeawayProductUpdateDTO;
import com.axin.system.domain.vo.TakeawayProductVO;
import com.axin.system.service.ITakeawayProductService;
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
@Tag(name = "后台外卖商品管理", description = "后台外卖商品管理接口")
@RestController
@RequestMapping("/errand/takeawayProduct")
public class TakeawayProductController extends BaseController {

    @Autowired
    private ITakeawayProductService productService;

    @Operation(summary = "分页查询商品列表", description = "按店铺、名称、类型、启用状态过滤")
    @PreAuthorize("@ss.hasPermi('errand:takeawayProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(TakeawayProductQueryDTO queryDTO) {
        startPage();
        TakeawayProduct query = new TakeawayProduct();
        query.setStoreId(queryDTO.getStoreId());
        query.setProductName(queryDTO.getProductName());
        query.setProductType(queryDTO.getProductType());
        query.setIsEnabled(queryDTO.getIsEnabled());
        List<TakeawayProduct> list = productService.selectTakeawayProductList(query);

        TableDataInfo data = getDataTable(list);
        List<TakeawayProductVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "查询商品详情")
    @PreAuthorize("@ss.hasPermi('errand:takeawayProduct:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        TakeawayProduct entity = productService.selectTakeawayProductById(id);
        return success(toVO(entity));
    }

    @Operation(summary = "新增商品")
    @PreAuthorize("@ss.hasPermi('errand:takeawayProduct:add')")
    @Log(title = "外卖商品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody TakeawayProductCreateDTO createDTO) {
        TakeawayProduct entity = new TakeawayProduct();
        entity.setStoreId(createDTO.getStoreId());
        entity.setProductName(createDTO.getProductName());
        entity.setPrice(createDTO.getPrice());
        entity.setImageUrl(createDTO.getImageUrl());
        entity.setProductType(createDTO.getProductType());
        entity.setIsEnabled(createDTO.getIsEnabled());
        return toAjax(productService.insertTakeawayProduct(entity));
    }

    @Operation(summary = "修改商品")
    @PreAuthorize("@ss.hasPermi('errand:takeawayProduct:edit')")
    @Log(title = "外卖商品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody TakeawayProductUpdateDTO updateDTO) {
        TakeawayProduct entity = new TakeawayProduct();
        entity.setId(updateDTO.getId());
        entity.setStoreId(updateDTO.getStoreId());
        entity.setProductName(updateDTO.getProductName());
        entity.setPrice(updateDTO.getPrice());
        entity.setImageUrl(updateDTO.getImageUrl());
        entity.setProductType(updateDTO.getProductType());
        entity.setIsEnabled(updateDTO.getIsEnabled());
        return toAjax(productService.updateTakeawayProduct(entity));
    }

    @Operation(summary = "删除商品")
    @PreAuthorize("@ss.hasPermi('errand:takeawayProduct:remove')")
    @Log(title = "外卖商品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "待删除ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(productService.deleteTakeawayProductByIds(ids));
    }

    private TakeawayProductVO toVO(TakeawayProduct p) {
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

