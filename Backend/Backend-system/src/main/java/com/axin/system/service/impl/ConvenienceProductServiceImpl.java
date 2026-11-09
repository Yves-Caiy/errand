package com.axin.system.service.impl;

import com.axin.system.domain.ConvenienceProduct;
import com.axin.system.mapper.ConvenienceProductMapper;
import com.axin.system.service.IConvenienceProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConvenienceProductServiceImpl implements IConvenienceProductService {

    private final ConvenienceProductMapper mapper;

    public ConvenienceProductServiceImpl(ConvenienceProductMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ConvenienceProduct selectConvenienceProductById(Long id) {
        return mapper.selectConvenienceProductById(id);
    }

    @Override
    public List<ConvenienceProduct> selectConvenienceProductList(ConvenienceProduct product) {
        return mapper.selectConvenienceProductList(product);
    }

    @Override
    public int insertConvenienceProduct(ConvenienceProduct product) {
        return mapper.insertConvenienceProduct(product);
    }

    @Override
    public int updateConvenienceProduct(ConvenienceProduct product) {
        return mapper.updateConvenienceProduct(product);
    }

    @Override
    public int deleteConvenienceProductByIds(Long[] ids) {
        return mapper.deleteConvenienceProductByIds(ids);
    }

    @Override
    public boolean isProductNameExists(String productName, Long excludeId) {
        ConvenienceProduct existed = mapper.selectConvenienceProductByName(productName);
        if (existed == null) {
            return false;
        }
        if (excludeId == null) {
            return true;
        }
        return !excludeId.equals(existed.getId());
    }
}

