package com.axin.errand.mapper;

import com.axin.errand.domain.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ShoppingCartMapper {
    int insertShoppingCart(ShoppingCart cart);
    int deleteShoppingCartById(Long id);
    int deleteShoppingCartByIds(@Param("ids") List<Long> ids, @Param("userId") Long userId);
    int updateShoppingCart(ShoppingCart cart);
    ShoppingCart selectShoppingCartById(Long id);
    List<ShoppingCart> selectShoppingCartList(ShoppingCart cart);
    int deleteShoppingCartByUserId(Long userId);
    ShoppingCart selectShoppingCartByUnique(@Param("userId") Long userId, @Param("productType") Integer productType, @Param("productId") Long productId, @Param("storeId") Long storeId);
}
