package com.axin.errand.service;

import com.axin.errand.domain.ShoppingCart;
import com.axin.errand.domain.vo.ShoppingCartVO;
import java.util.List;

public interface IShoppingCartService {
    /**
     * 添加到购物车
     */
    int addToCart(ShoppingCart cart);

    /**
     * 更新购物车
     */
    int updateCart(ShoppingCart cart);

    /**
     * 删除购物车
     */
    int deleteCartById(Long id);

    /**
     * 批量删除购物车
     */
    int deleteCartByIds(List<Long> ids, Long userId);

    /**
     * 查询购物车列表
     */
    List<ShoppingCartVO> listCart(Long userId, Integer productType);
}
