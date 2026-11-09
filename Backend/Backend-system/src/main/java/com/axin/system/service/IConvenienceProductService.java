package com.axin.system.service;

import com.axin.system.domain.ConvenienceProduct;

import java.util.List;

public interface IConvenienceProductService {
    ConvenienceProduct selectConvenienceProductById(Long id);

    List<ConvenienceProduct> selectConvenienceProductList(ConvenienceProduct product);

    int insertConvenienceProduct(ConvenienceProduct product);

    int updateConvenienceProduct(ConvenienceProduct product);

    int deleteConvenienceProductByIds(Long[] ids);

    boolean isProductNameExists(String productName, Long excludeId);
}

