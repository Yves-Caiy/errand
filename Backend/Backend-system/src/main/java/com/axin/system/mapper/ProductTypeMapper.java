package com.axin.system.mapper;

import com.axin.system.domain.ProductType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductTypeMapper {
    ProductType selectProductTypeById(@Param("id") Long id);

    ProductType selectProductTypeByName(@Param("typeName") String typeName);

    List<ProductType> selectProductTypeList(ProductType productType);

    int insertProductType(ProductType productType);

    int updateProductType(ProductType productType);

    int deleteProductTypeByIds(@Param("ids") Long[] ids);
}

