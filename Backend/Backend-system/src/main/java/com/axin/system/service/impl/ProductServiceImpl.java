package com.axin.system.service.impl;

import com.axin.system.domain.Product;
import com.axin.system.mapper.ProductMapper;
import com.axin.system.service.IProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    @Override
    public Product selectProductById(Long id) {
        return productMapper.selectProductById(id);
    }

    @Override
    public List<Product> selectProductList(Product product) {
        return productMapper.selectProductList(product);
    }

    @Override
    public int insertProduct(Product product) {
        return productMapper.insertProduct(product);
    }

    @Override
    public int updateProduct(Product product) {
        return productMapper.updateProduct(product);
    }

    @Override
    public int deleteProductByIds(Long[] ids) {
        return productMapper.deleteProductByIds(ids);
    }

    @Override
    public boolean isSpecNameExists(Long typeId, String specName, Long excludeId) {
        Product existed = productMapper.selectByTypeAndSpecName(typeId, specName);
        if (existed == null) {
            return false;
        }
        if (excludeId == null) {
            return true;
        }
        return !excludeId.equals(existed.getId());
    }
}
