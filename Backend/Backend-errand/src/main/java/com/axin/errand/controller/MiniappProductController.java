package com.axin.errand.controller;

import com.axin.common.annotation.Anonymous;
import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.system.domain.CommunityProductType;
import com.axin.system.domain.ConvenienceProduct;
import com.axin.system.domain.Product;
import com.axin.system.domain.ProductType;
import com.axin.system.domain.vo.ConvenienceProductVO;
import com.axin.system.domain.vo.ProductTypeVO;
import com.axin.system.domain.vo.ProductVO;
import com.axin.system.service.ICommunityProductTypeService;
import com.axin.system.service.IConvenienceProductService;
import com.axin.system.service.IProductService;
import com.axin.system.service.IProductTypeService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@Tag(name = "小程序商品接口")
@ApiSupport(author = "AXIN", order = 4)
@RestController
@RequestMapping("/miniapp")
public class MiniappProductController extends BaseController {

    @Autowired
    private IConvenienceProductService convenienceProductService;

    @Autowired
    private ICommunityProductTypeService communityProductTypeService;

    @Autowired
    private IProductTypeService productTypeService;

    @Autowired
    private IProductService productService;

    @Operation(summary = "获取便民超市商品列表")
    @ApiOperationSupport(order = 60)
    @Anonymous
    @GetMapping("/convenienceProduct/list")
    public AjaxResult convenienceProductList(@RequestParam(value = "productType", required = false) String productType) {
        ConvenienceProduct query = new ConvenienceProduct();
        query.setIsEnabled(1);
        query.setProductType(productType);
        return success(convenienceProductService.selectConvenienceProductList(query)
                .stream().map(this::toProductVO).collect(java.util.stream.Collectors.toList()));
    }

    @Operation(summary = "根据小区获取服务商品类型")
    @ApiOperationSupport(order = 70)
    @Anonymous
    @GetMapping("/productType/list")
    public AjaxResult productTypeList(@RequestParam("communityId") Long communityId) {
        CommunityProductType cpt = new CommunityProductType();
        cpt.setCommunityId(communityId);
        cpt.setIsEnabled(1);
        java.util.List<CommunityProductType> rel = communityProductTypeService.selectCommunityProductTypeList(cpt);
        java.util.Map<Long, Integer> orderMap = rel.stream().collect(java.util.stream.Collectors.toMap(CommunityProductType::getProductTypeId, r -> r.getSortOrder() == null ? 0 : r.getSortOrder(), (a, b) -> a));
        java.util.List<ProductTypeVO> types = rel.stream()
                .map(r -> productTypeService.selectProductTypeById(r.getProductTypeId()))
                .filter(t -> t != null && (t.getIsEnabled() == null || t.getIsEnabled() == 1))
                .map(this::toProductTypeVO)
                .sorted((a, b) -> java.util.Comparator.comparingInt(id -> orderMap.getOrDefault(((ProductTypeVO) id).getId(), 0)).compare(a, b))
                .collect(java.util.stream.Collectors.toList());
        return success(types);
    }

    @Operation(summary = "根据服务类型获取规格列表")
    @ApiOperationSupport(order = 80)
    @Anonymous
    @GetMapping("/product/list")
    public AjaxResult productList(@RequestParam("typeId") Long typeId) {
        Product query = new Product();
        query.setTypeId(typeId);
        query.setIsEnabled(1);
        java.util.List<ProductVO> list = productService.selectProductList(query).stream().map(this::toProductVO).collect(java.util.stream.Collectors.toList());
        return success(list);
    }

    private ConvenienceProductVO toProductVO(ConvenienceProduct p) {
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

    private ProductTypeVO toProductTypeVO(ProductType t) {
        if (t == null) return null;
        ProductTypeVO vo = new ProductTypeVO();
        vo.setId(t.getId());
        vo.setTypeName(t.getTypeName());
        vo.setTypeDesc(t.getTypeDesc());
        vo.setIconUrl(t.getIconUrl());
        vo.setIsManageable(t.getIsManageable());
        vo.setIsEnabled(t.getIsEnabled());
        vo.setCreateTime(t.getCreateTime());
        vo.setUpdateTime(t.getUpdateTime());
        return vo;
    }

    private ProductVO toProductVO(Product p) {
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
