package com.axin.errand.service.impl;

import com.axin.errand.domain.ShoppingCart;
import com.axin.errand.domain.vo.ShoppingCartVO;
import com.axin.errand.mapper.ShoppingCartMapper;
import com.axin.errand.service.IShoppingCartService;
import com.axin.system.domain.ConvenienceProduct;
import com.axin.system.domain.TakeawayProduct;
import com.axin.system.domain.TakeawayStore;
import com.axin.system.service.IConvenienceProductService;
import com.axin.system.service.ITakeawayProductService;
import com.axin.system.service.ITakeawayStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements IShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private IConvenienceProductService convenienceProductService;

    @Autowired
    private ITakeawayProductService takeawayProductService;

    @Autowired
    private ITakeawayStoreService takeawayStoreService;

    @Override
    public int addToCart(ShoppingCart cart) {
        // Check if exists
        ShoppingCart existing = shoppingCartMapper.selectShoppingCartByUnique(
                cart.getUserId(), cart.getProductType(), cart.getProductId(), cart.getStoreId());
        
        if (existing != null) {
            existing.setQuantity(existing.getQuantity() + cart.getQuantity());
            return shoppingCartMapper.updateShoppingCart(existing);
        } else {
            return shoppingCartMapper.insertShoppingCart(cart);
        }
    }

    @Override
    public int updateCart(ShoppingCart cart) {
        return shoppingCartMapper.updateShoppingCart(cart);
    }

    @Override
    public int deleteCartById(Long id) {
        return shoppingCartMapper.deleteShoppingCartById(id);
    }

    @Override
    public int deleteCartByIds(List<Long> ids, Long userId) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return shoppingCartMapper.deleteShoppingCartByIds(ids, userId);
    }

    @Override
    public List<ShoppingCartVO> listCart(Long userId, Integer productType) {
        ShoppingCart query = new ShoppingCart();
        query.setUserId(userId);
        query.setProductType(productType);
        List<ShoppingCart> list = shoppingCartMapper.selectShoppingCartList(query);
        
        List<ShoppingCartVO> result = new ArrayList<>();
        if (list == null || list.isEmpty()) {
            return result;
        }

        for (ShoppingCart item : list) {
            ShoppingCartVO vo = new ShoppingCartVO();
            vo.setId(item.getId());
            vo.setProductType(item.getProductType());
            vo.setProductId(item.getProductId());
            vo.setStoreId(item.getStoreId());
            vo.setQuantity(item.getQuantity());
            vo.setIsSelected(item.getIsSelected());
            
            if (item.getProductType() != null && item.getProductType() == 1) { // Supermarket
                ConvenienceProduct p = convenienceProductService.selectConvenienceProductById(item.getProductId());
                if (p != null) {
                    vo.setProductName(p.getProductName());
                    vo.setPrice(p.getPrice());
                    vo.setImageUrl(p.getImageUrl());
                }
            } else if (item.getProductType() != null && item.getProductType() == 2) { // Takeout
                TakeawayProduct p = takeawayProductService.selectTakeawayProductById(item.getProductId());
                if (p != null) {
                    vo.setProductName(p.getProductName());
                    vo.setPrice(p.getPrice());
                    vo.setImageUrl(p.getImageUrl());
                    
                    if (item.getStoreId() != null) {
                        TakeawayStore s = takeawayStoreService.selectTakeawayStoreById(item.getStoreId());
                        if (s != null) {
                            vo.setStoreName(s.getStoreName());
                        }
                    }
                }
            }
            result.add(vo);
        }
        return result;
    }
}
