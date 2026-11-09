package com.axin.system.service;

import com.axin.system.domain.ProductType;

import java.util.List;

public interface IProductTypeService {
    ProductType selectProductTypeById(Long id);

    List<ProductType> selectProductTypeList(ProductType productType);

    int insertProductType(ProductType productType);

    int updateProductType(ProductType productType);

    int deleteProductTypeByIds(Long[] ids);

    boolean isTypeNameExists(String typeName, Long excludeId);
}

