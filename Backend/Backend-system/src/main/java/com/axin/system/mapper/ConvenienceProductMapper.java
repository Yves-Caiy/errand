package com.axin.system.mapper;

import com.axin.system.domain.ConvenienceProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ConvenienceProductMapper {
    ConvenienceProduct selectConvenienceProductById(@Param("id") Long id);

    ConvenienceProduct selectConvenienceProductByName(@Param("productName") String productName);

    List<ConvenienceProduct> selectConvenienceProductList(ConvenienceProduct product);

    int insertConvenienceProduct(ConvenienceProduct product);

    int updateConvenienceProduct(ConvenienceProduct product);

    int deleteConvenienceProductByIds(@Param("ids") Long[] ids);
}

