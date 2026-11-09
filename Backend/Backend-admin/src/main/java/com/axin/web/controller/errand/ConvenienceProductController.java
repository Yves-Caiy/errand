package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.ConvenienceProduct;
import com.axin.system.domain.dto.ConvenienceProductCreateDTO;
import com.axin.system.domain.dto.ConvenienceProductQueryDTO;
import com.axin.system.domain.dto.ConvenienceProductUpdateDTO;
import com.axin.system.domain.vo.ConvenienceProductVO;
import com.axin.system.service.IConvenienceProductService;
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
@Tag(name = "后台便民超市商品管理", description = "后台便民超市商品管理接口")
@RestController
@RequestMapping("/errand/convenienceProduct")
public class ConvenienceProductController extends BaseController {

    @Autowired
    private IConvenienceProductService productService;

    @Operation(summary = "分页查询商品列表", description = "按名称、类型、启用状态过滤")
    @PreAuthorize("@ss.hasPermi('errand:convenienceProduct:list')")
    @GetMapping("/list")
    public TableDataInfo list(ConvenienceProductQueryDTO queryDTO) {
        startPage();
        ConvenienceProduct query = new ConvenienceProduct();
        query.setProductName(queryDTO.getProductName());
        query.setProductType(queryDTO.getProductType());
        query.setIsEnabled(queryDTO.getIsEnabled());
        List<ConvenienceProduct> list = productService.selectConvenienceProductList(query);

        TableDataInfo data = getDataTable(list);
        List<ConvenienceProductVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "查询商品详情")
    @PreAuthorize("@ss.hasPermi('errand:convenienceProduct:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        ConvenienceProduct entity = productService.selectConvenienceProductById(id);
        return success(toVO(entity));
    }

    @Operation(summary = "新增商品")
    @PreAuthorize("@ss.hasPermi('errand:convenienceProduct:add')")
    @Log(title = "便民超市商品管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody ConvenienceProductCreateDTO createDTO) {
        if (productService.isProductNameExists(createDTO.getProductName(), null)) {
            return error("新增失败：商品名称已存在");
        }
        ConvenienceProduct entity = new ConvenienceProduct();
        entity.setProductName(createDTO.getProductName());
        entity.setPrice(createDTO.getPrice());
        entity.setImageUrl(createDTO.getImageUrl());
        entity.setProductType(createDTO.getProductType());
        entity.setIsEnabled(createDTO.getIsEnabled());
        return toAjax(productService.insertConvenienceProduct(entity));
    }

    @Operation(summary = "修改商品")
    @PreAuthorize("@ss.hasPermi('errand:convenienceProduct:edit')")
    @Log(title = "便民超市商品管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody ConvenienceProductUpdateDTO updateDTO) {
        if (updateDTO.getProductName() != null
                && productService.isProductNameExists(updateDTO.getProductName(), updateDTO.getId())) {
            return error("修改失败：商品名称已存在");
        }
        ConvenienceProduct entity = new ConvenienceProduct();
        entity.setId(updateDTO.getId());
        entity.setProductName(updateDTO.getProductName());
        entity.setPrice(updateDTO.getPrice());
        entity.setImageUrl(updateDTO.getImageUrl());
        entity.setProductType(updateDTO.getProductType());
        entity.setIsEnabled(updateDTO.getIsEnabled());
        return toAjax(productService.updateConvenienceProduct(entity));
    }

    @Operation(summary = "删除商品")
    @PreAuthorize("@ss.hasPermi('errand:convenienceProduct:remove')")
    @Log(title = "便民超市商品管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "待删除ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(productService.deleteConvenienceProductByIds(ids));
    }

    private ConvenienceProductVO toVO(ConvenienceProduct p) {
        if (p == null) return null;
        ConvenienceProductVO vo = new ConvenienceProductVO();
        vo.setId(p.getId());
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

