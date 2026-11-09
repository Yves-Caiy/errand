package com.axin.system.mapper;

import com.axin.system.domain.TakeawayProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TakeawayProductMapper {
    TakeawayProduct selectTakeawayProductById(@Param("id") Long id);

    List<TakeawayProduct> selectTakeawayProductList(TakeawayProduct product);

    int insertTakeawayProduct(TakeawayProduct product);

    int updateTakeawayProduct(TakeawayProduct product);

    int deleteTakeawayProductByIds(@Param("ids") Long[] ids);
}

