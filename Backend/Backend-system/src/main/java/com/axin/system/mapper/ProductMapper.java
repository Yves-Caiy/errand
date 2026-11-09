package com.axin.system.mapper;

import com.axin.system.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductMapper {
    Product selectProductById(@Param("id") Long id);

    List<Product> selectProductList(Product product);

    int insertProduct(Product product);

    int updateProduct(Product product);

    int deleteProductByIds(@Param("ids") Long[] ids);

    Product selectByTypeAndSpecName(@Param("typeId") Long typeId, @Param("specName") String specName);
}
