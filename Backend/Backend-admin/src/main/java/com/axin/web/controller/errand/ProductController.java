package com.axin.web.controller.errand;

import com.axin.common.annotation.Log;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.common.enums.BusinessType;
import com.axin.system.domain.Product;
import com.axin.system.domain.dto.ProductCreateDTO;
import com.axin.system.domain.dto.ProductQueryDTO;
import com.axin.system.domain.dto.ProductUpdateDTO;
import com.axin.system.domain.vo.ProductVO;
import com.axin.system.service.IProductService;
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
@Tag(name = "后台商品规格管理", description = "后台商品（服务子项）规格管理接口")
@RestController
@RequestMapping("/errand/product")
public class ProductController extends BaseController {

    @Autowired
    private IProductService productService;

    @Operation(summary = "分页查询商品规格列表", description = "按类型、名称、启用状态过滤")
    @PreAuthorize("@ss.hasPermi('errand:product:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductQueryDTO queryDTO) {
        startPage();
        Product query = new Product();
        query.setTypeId(queryDTO.getTypeId());
        query.setSpecName(queryDTO.getSpecName());
        query.setIsUniversalCustom(queryDTO.getIsUniversalCustom());
        query.setIsEnabled(queryDTO.getIsEnabled());
        List<Product> list = productService.selectProductList(query);

        TableDataInfo data = getDataTable(list);
        List<ProductVO> voList = list.stream().map(this::toVO).collect(Collectors.toList());
        data.setRows(voList);
        return data;
    }

    @Operation(summary = "查询商品规格详情")
    @PreAuthorize("@ss.hasPermi('errand:product:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@Parameter(description = "主键ID") @PathVariable @NotNull Long id) {
        Product entity = productService.selectProductById(id);
        return success(toVO(entity));
    }

    @Operation(summary = "新增商品规格")
    @PreAuthorize("@ss.hasPermi('errand:product:add')")
    @Log(title = "商品规格管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Valid @RequestBody ProductCreateDTO createDTO) {
        if (productService.isSpecNameExists(createDTO.getTypeId(), createDTO.getSpecName(), null)) {
            return error("新增失败：同一服务模块下规格名称已存在");
        }
        Product entity = new Product();
        entity.setTypeId(createDTO.getTypeId());
        entity.setSpecName(createDTO.getSpecName());
        entity.setWeightMin(createDTO.getWeightMin());
        entity.setWeightMax(createDTO.getWeightMax());
        entity.setDistance(createDTO.getDistance());
        entity.setAmountMin(createDTO.getAmountMin());
        entity.setAmountMax(createDTO.getAmountMax());
        entity.setDuration(createDTO.getDuration());
        entity.setDurationUnit(createDTO.getDurationUnit());
        entity.setPriceMin(createDTO.getPriceMin());
        entity.setPriceMax(createDTO.getPriceMax());
        entity.setIsUniversalCustom(createDTO.getIsUniversalCustom());
        entity.setIsEnabled(createDTO.getIsEnabled());
        return toAjax(productService.insertProduct(entity));
    }

    @Operation(summary = "修改商品规格")
    @PreAuthorize("@ss.hasPermi('errand:product:edit')")
    @Log(title = "商品规格管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Valid @RequestBody ProductUpdateDTO updateDTO) {
        if (updateDTO.getTypeId() != null && updateDTO.getSpecName() != null
                && productService.isSpecNameExists(updateDTO.getTypeId(), updateDTO.getSpecName(), updateDTO.getId())) {
            return error("修改失败：同一服务模块下规格名称已存在");
        }
        Product entity = new Product();
        entity.setId(updateDTO.getId());
        entity.setTypeId(updateDTO.getTypeId());
        entity.setSpecName(updateDTO.getSpecName());
        entity.setWeightMin(updateDTO.getWeightMin());
        entity.setWeightMax(updateDTO.getWeightMax());
        entity.setDistance(updateDTO.getDistance());
        entity.setAmountMin(updateDTO.getAmountMin());
        entity.setAmountMax(updateDTO.getAmountMax());
        entity.setDuration(updateDTO.getDuration());
        entity.setDurationUnit(updateDTO.getDurationUnit());
        entity.setPriceMin(updateDTO.getPriceMin());
        entity.setPriceMax(updateDTO.getPriceMax());
        entity.setIsUniversalCustom(updateDTO.getIsUniversalCustom());
        entity.setIsEnabled(updateDTO.getIsEnabled());
        return toAjax(productService.updateProduct(entity));
    }

    @Operation(summary = "删除商品规格")
    @PreAuthorize("@ss.hasPermi('errand:product:remove')")
    @Log(title = "商品规格管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@Parameter(description = "待删除ID集合，逗号分隔") @PathVariable Long[] ids) {
        return toAjax(productService.deleteProductByIds(ids));
    }

    private ProductVO toVO(Product p) {
        if (p == null) return null;
        ProductVO vo = new ProductVO();
        vo.setId(p.getId());
        vo.setTypeId(p.getTypeId());
        vo.setSpecName(p.getSpecName());
        vo.setWeightMin(p.getWeightMin());
        vo.setWeightMax(p.getWeightMax());
        vo.setDistance(p.getDistance());
        vo.setAmountMin(p.getAmountMin());
        vo.setAmountMax(p.getAmountMax());
        vo.setDuration(p.getDuration());
        vo.setDurationUnit(p.getDurationUnit());
        vo.setPriceMin(p.getPriceMin());
        vo.setPriceMax(p.getPriceMax());
        vo.setIsUniversalCustom(p.getIsUniversalCustom());
        vo.setIsEnabled(p.getIsEnabled());
        vo.setCreateTime(p.getCreateTime());
        vo.setUpdateTime(p.getUpdateTime());
        return vo;
    }
}
