package com.axin.system.service;

import com.axin.system.domain.TakeawayProduct;

import java.util.List;

public interface ITakeawayProductService {
    TakeawayProduct selectTakeawayProductById(Long id);

    List<TakeawayProduct> selectTakeawayProductList(TakeawayProduct product);

    int insertTakeawayProduct(TakeawayProduct product);

    int updateTakeawayProduct(TakeawayProduct product);

    int deleteTakeawayProductByIds(Long[] ids);
}

