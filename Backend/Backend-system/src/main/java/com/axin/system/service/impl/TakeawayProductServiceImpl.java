package com.axin.system.service.impl;

import com.axin.system.domain.TakeawayProduct;
import com.axin.system.mapper.TakeawayProductMapper;
import com.axin.system.service.ITakeawayProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TakeawayProductServiceImpl implements ITakeawayProductService {

    private final TakeawayProductMapper mapper;

    public TakeawayProductServiceImpl(TakeawayProductMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public TakeawayProduct selectTakeawayProductById(Long id) {
        return mapper.selectTakeawayProductById(id);
    }

    @Override
    public List<TakeawayProduct> selectTakeawayProductList(TakeawayProduct product) {
        return mapper.selectTakeawayProductList(product);
    }

    @Override
    public int insertTakeawayProduct(TakeawayProduct product) {
        return mapper.insertTakeawayProduct(product);
    }

    @Override
    public int updateTakeawayProduct(TakeawayProduct product) {
        return mapper.updateTakeawayProduct(product);
    }

    @Override
    public int deleteTakeawayProductByIds(Long[] ids) {
        return mapper.deleteTakeawayProductByIds(ids);
    }
}

