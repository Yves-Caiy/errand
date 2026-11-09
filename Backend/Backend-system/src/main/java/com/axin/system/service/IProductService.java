package com.axin.system.service;

import com.axin.system.domain.Product;

import java.util.List;

public interface IProductService {
    Product selectProductById(Long id);

    List<Product> selectProductList(Product product);

    int insertProduct(Product product);

    int updateProduct(Product product);

    int deleteProductByIds(Long[] ids);

    boolean isSpecNameExists(Long typeId, String specName, Long excludeId);
}
