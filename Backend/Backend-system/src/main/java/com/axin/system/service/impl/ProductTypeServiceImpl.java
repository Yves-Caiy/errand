package com.axin.system.service.impl;

import com.axin.system.domain.ProductType;
import com.axin.system.mapper.ProductTypeMapper;
import com.axin.system.service.IProductTypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductTypeServiceImpl implements IProductTypeService {

    private final ProductTypeMapper productTypeMapper;

    public ProductTypeServiceImpl(ProductTypeMapper productTypeMapper) {
        this.productTypeMapper = productTypeMapper;
    }

    @Override
    public ProductType selectProductTypeById(Long id) {
        return productTypeMapper.selectProductTypeById(id);
    }

    @Override
    public List<ProductType> selectProductTypeList(ProductType productType) {
        return productTypeMapper.selectProductTypeList(productType);
    }

    @Override
    public int insertProductType(ProductType productType) {
        return productTypeMapper.insertProductType(productType);
    }

    @Override
    public int updateProductType(ProductType productType) {
        return productTypeMapper.updateProductType(productType);
    }

    @Override
    public int deleteProductTypeByIds(Long[] ids) {
        return productTypeMapper.deleteProductTypeByIds(ids);
    }

    @Override
    public boolean isTypeNameExists(String typeName, Long excludeId) {
        ProductType existed = productTypeMapper.selectProductTypeByName(typeName);
        if (existed == null) {
            return false;
        }
        if (excludeId == null) {
            return true;
        }
        return !excludeId.equals(existed.getId());
    }
}

