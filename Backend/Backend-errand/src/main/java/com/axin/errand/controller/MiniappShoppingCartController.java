package com.axin.errand.controller;

import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.errand.domain.MiniLoginUser;
import com.axin.errand.domain.ShoppingCart;
import com.axin.errand.service.IShoppingCartService;
import com.axin.errand.service.MiniTokenService;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@Tag(name = "小程序购物车接口")
@ApiSupport(author = "AXIN", order = 8)
@RestController
@RequestMapping("/miniapp/cart")
public class MiniappShoppingCartController extends BaseController {

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private MiniTokenService tokenService;

    @Operation(summary = "添加购物车")
    @ApiOperationSupport(order = 210)
    @PostMapping("/add")
    public AjaxResult addCart(HttpServletRequest request, @RequestBody ShoppingCart dto) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        dto.setUserId(loginUser.getUserId());
        return toAjax(shoppingCartService.addToCart(dto));
    }

    @Operation(summary = "添加外卖购物车")
    @ApiOperationSupport(order = 211)
    @PostMapping("/addTakeout")
    public AjaxResult addTakeoutCart(HttpServletRequest request, @RequestBody ShoppingCart dto) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        if (dto.getStoreId() == null) {
            return error("店铺ID不能为空");
        }
        dto.setUserId(loginUser.getUserId());
        dto.setProductType(2); // 外卖商品
        return toAjax(shoppingCartService.addToCart(dto));
    }

    @Operation(summary = "添加超市购物车")
    @ApiOperationSupport(order = 212)
    @PostMapping("/addSupermarket")
    public AjaxResult addSupermarketCart(HttpServletRequest request, @RequestBody ShoppingCart dto) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        dto.setUserId(loginUser.getUserId());
        dto.setProductType(1); // 超市商品
        return toAjax(shoppingCartService.addToCart(dto));
    }

    @Operation(summary = "查询购物车列表")
    @ApiOperationSupport(order = 220)
    @GetMapping("/list")
    public AjaxResult listCart(HttpServletRequest request, @RequestParam(value = "productType") Integer productType) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        return success(shoppingCartService.listCart(loginUser.getUserId(), productType));
    }

    @Operation(summary = "更新购物车")
    @ApiOperationSupport(order = 225)
    @PutMapping("/update")
    public AjaxResult updateCart(HttpServletRequest request, @RequestBody ShoppingCart dto) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        dto.setUserId(loginUser.getUserId());
        return toAjax(shoppingCartService.updateCart(dto));
    }

    @Operation(summary = "删除购物车商品")
    @ApiOperationSupport(order = 230)
    @DeleteMapping("/{ids}")
    public AjaxResult removeCart(HttpServletRequest request, @PathVariable Long[] ids) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        return toAjax(shoppingCartService.deleteCartByIds(java.util.Arrays.asList(ids), loginUser.getUserId()));
    }
}
