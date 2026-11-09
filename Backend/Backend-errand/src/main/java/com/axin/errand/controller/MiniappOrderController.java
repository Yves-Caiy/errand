package com.axin.errand.controller;

import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.common.core.page.TableDataInfo;
import com.axin.errand.domain.MiniLoginUser;
import com.axin.errand.service.MiniTokenService;
import com.axin.system.domain.*;
import com.axin.system.domain.dto.OrderCreateDTO;
import com.axin.system.domain.dto.OrderItemCreateDTO;
import com.axin.system.domain.vo.OrderVO;
import com.axin.system.service.*;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.util.Map;
import java.util.HashMap;
import com.axin.errand.service.IShoppingCartService;
 
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.axin.system.domain.dto.OrderPrintFileSaveDTO;
import com.axin.common.utils.file.ImageUtils;
import com.axin.common.utils.file.FileUtils;
import java.net.URLEncoder;

@Validated
@Tag(name = "小程序订单接口")
@ApiSupport(author = "AXIN", order = 7)
@RestController
@RequestMapping("/miniapp/order")
public class MiniappOrderController extends BaseController {

    @Autowired
    private IOrderPrintFileService orderPrintFileService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private MiniTokenService tokenService;

    @Autowired
    private IProductService productService;

    @Autowired
    private IServicePackageService servicePackageService;
    
    @Autowired
    private IWechatUserService wechatUserService;

    @Autowired
    private ICommunityService communityService;

    @Autowired
    private IShoppingCartService shoppingCartService;

    @Autowired
    private IMiniappConfigService miniappConfigService;

    @Autowired
    private com.axin.errand.service.DispatchService dispatchService;

    

    @Operation(summary = "获取其他服务待接单订单列表")
    @ApiOperationSupport(order = 120)
    @GetMapping("/list")
    public AjaxResult orderList(@RequestParam(value = "serviceType", required = false) String serviceType,
                                @RequestParam(value = "communityId", required = false) Long communityId) {
        startPage();
        OrderMain query = new OrderMain();
        query.setOrderType(4);
        query.setOrderStatus(1);
        query.setPayStatus(1);
        query.setServiceType(serviceType);
        query.setCommunityId(communityId);
        java.util.List<OrderMain> list = orderService.selectOrderMainList(query);
        java.util.List<OrderVO> volist = list.stream()
                .map(this::toOrderVOWithoutItems)
                .collect(java.util.stream.Collectors.toList());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("rows", volist);
        ajax.put("total", new com.github.pagehelper.PageInfo(list).getTotal());
        return ajax;
    }

    @Operation(summary = "获取我的订单列表")
    @ApiOperationSupport(order = 130)
    @GetMapping("/myList")
    public AjaxResult myOrderList(HttpServletRequest request, @RequestParam(value = "orderStatus", required = false) Integer orderStatus) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        startPage();
        OrderMain query = new OrderMain();
        query.setUserId(loginUser.getUserId());
        query.setOrderStatus(orderStatus);
        java.util.List<OrderMain> list = orderService.selectOrderMainList(query);
        java.util.List<OrderVO> volist = list.stream()
                .map(this::toOrderVOWithoutItems)
                .collect(java.util.stream.Collectors.toList());
        AjaxResult ajax = AjaxResult.success();
        ajax.put("rows", volist);
        ajax.put("total", new com.github.pagehelper.PageInfo(list).getTotal());
        return ajax;
    }

    @Operation(summary = "取消订单")
    @ApiOperationSupport(order = 140)
    @PutMapping("/cancel")
    public AjaxResult cancelOrder(HttpServletRequest request, @RequestParam("id") Long id, @RequestParam(value = "reason", required = false) String reason) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        OrderMain existed = orderService.selectOrderMainById(id);
        if (existed == null || !loginUser.getUserId().equals(existed.getUserId())) {
            return error("无权取消");
        }
        if (existed.getOrderStatus() == null || existed.getOrderStatus() != 0 || 
            existed.getPayStatus() == null || existed.getPayStatus() != 0) {
            return error("当前状态不可取消");
        }
        OrderMain upd = new OrderMain();
        upd.setId(id);
        upd.setOrderStatus(4);
        upd.setCancelReason(reason);
        orderService.updateOrderMain(upd);
        return success(true);
    }

    @Operation(summary = "申请退款")
    @ApiOperationSupport(order = 145)
    @PutMapping("/refund")
    public AjaxResult refundOrder(HttpServletRequest request, @RequestParam("id") Long id, @RequestParam(value = "reason", required = false) String reason) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        OrderMain existed = orderService.selectOrderMainById(id);
        if (existed == null || !loginUser.getUserId().equals(existed.getUserId())) {
            return error("无权操作");
        }
        Integer os = existed.getOrderStatus();
        if (os == null || (os != 1 && os != 2 && os != 3) || 
            existed.getPayStatus() == null || existed.getPayStatus() != 1) {
            return error("当前订单状态不可申请退款");
        }
        OrderMain upd = new OrderMain();
        upd.setId(id);
        upd.setPayStatus(2); // 退款中
        upd.setCancelReason(reason);
        orderService.updateOrderMain(upd);
        return success(true);
    }

    @Operation(summary = "创建其他服务/快递订单")
    @ApiOperationSupport(order = 151)
    @PostMapping("/create/other")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult createOtherOrder(HttpServletRequest request, @RequestBody OrderCreateDTO dto) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        WechatUser user = wechatUserService.selectWechatUserById(loginUser.getUserId());
        if (user == null || user.getCommunityId() == null) {
            return error("请先绑定小区");
        }

        OrderMain order = new OrderMain();
        order.setOrderNo(genOrderNo(4)); // 4 for Other
        order.setUserId(loginUser.getUserId());
        order.setCommunityId(user.getCommunityId());
        order.setOrderType(4);
        
        order.setDetailAddress(dto.getDetailAddress());
        order.setServiceType(dto.getServiceType()); 
        order.setStartAddress(dto.getStartAddress());
        order.setEndAddress(dto.getEndAddress());
        order.setBudget(dto.getBudget());
        order.setSpecName(dto.getSpecName());
        order.setWechatId(dto.getWechatId());
        
        long total = dto.getTotalAmount() == null ? 0L : dto.getTotalAmount();
        order.setTotalAmount(total);
        long discount = dto.getDiscountAmount() == null ? 0L : dto.getDiscountAmount();
        order.setDiscountAmount(discount);
        order.setActualAmount(dto.getActualAmount() == null ? (total - discount) : dto.getActualAmount());
        order.setCommissionAmount(dto.getCommissionAmount() == null ? 0L : dto.getCommissionAmount());
        
        order.setNote(dto.getNote());
        
        order.setOrderStatus(0); // 待支付
        order.setPayStatus(0); // 未支付
        order.setPayMethod(1); // 微信支付

        orderService.insertOrderMain(order);
        
        Map<String, Object> res = new HashMap<>();
        res.put("id", order.getId());
        res.put("orderNo", order.getOrderNo());
        return success(res);
    }

    @Operation(summary = "保存打印订单文件")
    @PostMapping("/save/print-files")
    public AjaxResult savePrintFiles(@RequestBody OrderPrintFileSaveDTO dto) {
        if (dto.getOrderId() == null || dto.getFiles() == null || dto.getFiles().isEmpty()) {
            return error("参数错误");
        }
        for (OrderPrintFileSaveDTO.FileInfo file : dto.getFiles()) {
            OrderPrintFile opf = new OrderPrintFile();
            opf.setOrderId(dto.getOrderId());
            opf.setFileUrl(file.getUrl());
            opf.setFileName(file.getName());
            orderPrintFileService.insertOrderPrintFile(opf);
        }
        return success();
    }

    @Operation(summary = "查询订单打印文件列表")
    @ApiOperationSupport(order = 220)
    @GetMapping("/print-files")
    public AjaxResult listPrintFiles(HttpServletRequest request, @RequestParam("orderId") Long orderId) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        OrderMain order = orderService.selectOrderMainById(orderId);
        if (order == null) {
            return error("订单不存在");
        }
        if (!loginUser.getUserId().equals(order.getUserId()) && !loginUser.getUserId().equals(order.getRunnerId())) {
            return error("无权查看");
        }
        OrderPrintFile query = new OrderPrintFile();
        query.setOrderId(orderId);
        java.util.List<OrderPrintFile> list = orderPrintFileService.selectOrderPrintFileList(query);
        java.util.List<java.util.Map<String, Object>> rows = new java.util.ArrayList<>();
        if (list != null) {
            for (OrderPrintFile f : list) {
                java.util.Map<String, Object> m = new java.util.HashMap<>();
                m.put("id", f.getId());
                m.put("fileUrl", f.getFileUrl());
                m.put("fileName", f.getFileName());
                rows.add(m);
            }
        }
        AjaxResult ajax = AjaxResult.success();
        ajax.put("rows", rows);
        ajax.put("total", rows.size());
        return ajax;
    }

    @Operation(summary = "下载订单打印文件")
    @ApiOperationSupport(order = 225)
    @GetMapping("/print-files/download/{id}")
    public void downloadPrintFile(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Long id) {
        try {
            MiniLoginUser loginUser = tokenService.getLoginUser(request);
            if (loginUser == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "未登录");
                return;
            }
            OrderPrintFile file = orderPrintFileService.selectOrderPrintFileById(id);
            if (file == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件不存在");
                return;
            }
            OrderMain order = orderService.selectOrderMainById(file.getOrderId());
            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "订单不存在");
                return;
            }
            if (!loginUser.getUserId().equals(order.getUserId()) && !loginUser.getUserId().equals(order.getRunnerId())) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "无权下载");
                return;
            }
            String url = file.getFileUrl();
            byte[] data = ImageUtils.readFile(url);
            if (data == null || data.length == 0) {
                // 如果读取失败，尝试重定向到原始URL
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    response.sendRedirect(url);
                    return;
                }
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "文件读取失败");
                return;
            }
            String name = file.getFileName();
            if (name == null || name.isEmpty()) {
                name = FileUtils.getName(url);
            }
            String encoded = URLEncoder.encode(name, java.nio.charset.StandardCharsets.UTF_8);
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encoded);
            response.setContentLength(data.length);
            java.io.OutputStream os = response.getOutputStream();
            os.write(data);
            os.flush();
        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "下载失败");
            } catch (Exception ignored) { }
        }
    }

    @Operation(summary = "创建订单")
    @ApiOperationSupport(order = 150)
    @PostMapping("/create")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult createOrder(HttpServletRequest request, @Valid @RequestBody OrderCreateDTO dto) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        Integer type = dto.getOrderType();
        if (type == null || type < 1 || type > 4) {
            return error("订单类型不合法");
        }
        OrderMain order = new OrderMain();
        order.setOrderNo(genOrderNo(type));
        order.setUserId(loginUser.getUserId());
        order.setRunnerId(null);
        order.setOrderType(type);
        order.setDetailAddress(dto.getDetailAddress());
        order.setServiceType(dto.getServiceType());
        order.setStartAddress(dto.getStartAddress());
        order.setEndAddress(dto.getEndAddress());
        order.setDeliveryAddress(dto.getDeliveryAddress());
        order.setItemDesc(dto.getItemDesc());
        order.setBudget(dto.getBudget());
        order.setSpecName(dto.getSpecName());
        order.setWechatId(dto.getWechatId());
        order.setDurationType(dto.getDurationType());
        order.setDurationValue(dto.getDurationValue());

        long total = 0L;
        java.util.List<OrderItemCreateDTO> items = dto.getItems();
        
        if (type == 1 || type == 2) {
            // 外卖或超市订单
            WechatUser user = wechatUserService.selectWechatUserById(loginUser.getUserId());
            if (user == null || user.getCommunityId() == null) {
                return error("请先绑定小区");
            }
            order.setCommunityId(user.getCommunityId());
            
            if (dto.getDetailAddress() == null) return error("详细地址不能为空");
            if (dto.getTotalAmount() == null) return error("总金额不能为空");
            if (dto.getActualAmount() == null) return error("实付金额不能为空");
            if (dto.getCommissionAmount() == null) return error("佣金不能为空");
            if (dto.getPayMethod() == null || dto.getPayMethod() != 1) return error("支付方式仅支持微信支付");
            
            order.setTotalAmount(dto.getTotalAmount());
            order.setActualAmount(dto.getActualAmount());
            order.setCommissionAmount(dto.getCommissionAmount());
            order.setPayMethod(dto.getPayMethod());
            order.setOrderStatus(0);
            order.setNote(dto.getNote());
            
            if (items == null || items.isEmpty()) {
                return error("订单项不能为空");
            }
        } else if (type == 4) {
            order.setCommunityId(dto.getCommunityId());
            total = dto.getBudget() == null ? 0L : dto.getBudget();
            order.setTotalAmount(total);
            long discount = dto.getDiscountAmount() == null ? 0L : dto.getDiscountAmount();
            long actual = total - discount;
            order.setActualAmount(actual < 0 ? 0 : actual);
            order.setCommissionAmount(0L);
            order.setOrderStatus(1);
        } else {
            order.setCommunityId(dto.getCommunityId());
            if (items == null || items.isEmpty()) {
                return error("订单项不能为空");
            }
            for (OrderItemCreateDTO it : items) {
                long qty = it.getQuantity() == null ? 0 : it.getQuantity();
                long price = it.getUnitPrice() == null ? 0 : it.getUnitPrice();
                long sub = it.getSubtotal() == null ? (qty * price) : it.getSubtotal();
                total += sub;
            }
            order.setTotalAmount(total);
            long discount = dto.getDiscountAmount() == null ? 0L : dto.getDiscountAmount();
            long actual = total - discount;
            order.setActualAmount(actual < 0 ? 0 : actual);
            order.setCommissionAmount(0L);
            order.setOrderStatus(0);
            order.setNote(dto.getNote());
            order.setPayMethod(1);
        }
        
        order.setDiscountAmount(dto.getDiscountAmount() == null ? 0L : dto.getDiscountAmount());
        order.setPayStatus(0);
        orderService.insertOrderMain(order);

        if (type != 4) {
            for (OrderItemCreateDTO it : items) {
                OrderItem item = new OrderItem();
                item.setOrderId(order.getId());
                item.setItemType(it.getItemType() != null ? it.getItemType() : type);
                item.setPackageId(it.getPackageId());
                item.setProductId(it.getProductId());
                String name = it.getProductName();
                if ((name == null || name.isEmpty()) && it.getProductId() != null) {
                    Product p = productService.selectProductById(it.getProductId());
                    if (p != null) name = p.getSpecName();
                }
                if ((name == null || name.isEmpty()) && it.getPackageId() != null) {
                    ServicePackage sp = servicePackageService.selectServicePackageById(it.getPackageId());
                    if (sp != null) name = sp.getPackageName();
                }
                item.setProductName(name);
                item.setQuantity(it.getQuantity());
                item.setUnitPrice(it.getUnitPrice());
                long qty = it.getQuantity() == null ? 0 : it.getQuantity();
                long price = it.getUnitPrice() == null ? 0 : it.getUnitPrice();
                long sub = it.getSubtotal() == null ? (qty * price) : it.getSubtotal();
                item.setSubtotal(sub);
                item.setStoreId(it.getStoreId());
                orderService.insertOrderItem(item);
            }
            
            // 删除购物车对应商品
            Integer cartType = null;
            if (type == 1) {
                cartType = 2; // 外卖订单(1) -> 外卖购物车(2)
            } else if (type == 2) {
                cartType = 1; // 超市订单(2) -> 超市购物车(1)
            }

            if (cartType != null) {
                java.util.List<com.axin.errand.domain.vo.ShoppingCartVO> cartList = shoppingCartService.listCart(loginUser.getUserId(), cartType);
                if (cartList != null && !cartList.isEmpty()) {
                     for (OrderItemCreateDTO item : items) {
                          cartList.stream()
                              .filter(c -> c.getProductId() != null && c.getProductId().equals(item.getProductId()))
                              .findFirst()
                              .ifPresent(c -> shoppingCartService.deleteCartById(c.getId()));
                     }
                }
            }
        }
        Map<String, Object> res = new HashMap<>();
        res.put("id", order.getId());
        res.put("orderNo", order.getOrderNo());
        return success(res);
    }

    @Operation(summary = "用户接单")
    @ApiOperationSupport(order = 160)
    @PutMapping("/accept")
    public AjaxResult acceptOrder(HttpServletRequest request, @RequestParam("id") Long id) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        OrderMain existed = orderService.selectOrderMainById(id);
        if (existed == null) {
            return error("订单不存在");
        }
        // 测试模式开启时接单不作限制，可以接自己的订单
        if (!isTestMode() && loginUser.getUserId().equals(existed.getUserId())) {
            return error("不能接自己的订单");
        }
        if (existed.getOrderStatus() == null || existed.getOrderStatus() != 1 || existed.getRunnerId() != null) {
            return error("订单不可接单");
        }
        
        // 条件更新（WHERE order_status=1 AND runner_id IS NULL）保证并发互斥，并收敛派单记录
        int rows = dispatchService.acceptOrder(id, loginUser.getUserId());

        if (rows == 0) {
            return error("订单已被其他人接单");
        }
        OrderMain updated = orderService.selectOrderMainById(id);
        return success(toOrderVOWithoutItems(updated));
    }

    /**
     * 是否开启测试模式：开启后接单不作限制（可以接自己的订单）
     */
    private boolean isTestMode() {
        try {
            MiniappConfig cfg = miniappConfigService.getCurrent();
            return cfg != null && cfg.getTestMode() != null && cfg.getTestMode() == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @Operation(summary = "确认送达/完成订单")
    @ApiOperationSupport(order = 170)
    @PutMapping("/complete")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult completeOrder(HttpServletRequest request, @RequestParam("id") Long id) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        OrderMain existed = orderService.selectOrderMainById(id);
        if (existed == null) {
            return error("订单不存在");
        }
        if (!loginUser.getUserId().equals(existed.getUserId())) {
            return error("无权操作");
        }
        if (existed.getOrderStatus() == null || existed.getOrderStatus() != 2) {
            return error("当前状态不可完成");
        }
        
        OrderMain upd = new OrderMain();
        upd.setId(id);
        upd.setOrderStatus(3);
        upd.setFinishTime(new java.util.Date());
        orderService.updateOrderMain(upd);

        // 更新接单员的佣金
        Long runnerId = existed.getRunnerId();
        if (runnerId != null) {
            WechatUser runner = wechatUserService.selectWechatUserById(runnerId);
            if (runner != null) {
                long amount = existed.getActualAmount() == null ? 0L : existed.getActualAmount();
                WechatUser userUpd = new WechatUser();
                userUpd.setId(runnerId);
                userUpd.setTotalCommission((runner.getTotalCommission() == null ? 0L : runner.getTotalCommission()) + amount);
                userUpd.setPendingCommission((runner.getPendingCommission() == null ? 0L : runner.getPendingCommission()) + amount);
                wechatUserService.updateWechatUser(userUpd);
            }
        }

        return success(true);
    }


    @Operation(summary = "获取我已接的订单列表")
    @ApiOperationSupport(order = 180)
    @GetMapping("/acceptedList")
    public AjaxResult acceptedOrderList(HttpServletRequest request, @RequestParam(value = "orderStatus", required = false) Integer orderStatus) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        OrderMain query = new OrderMain();
        query.setRunnerId(loginUser.getUserId());
        query.setOrderStatus(orderStatus);
        java.util.List<OrderVO> list = orderService.selectOrderMainList(query)
                .stream()
                .map(this::toOrderVOWithoutItems)
                .collect(java.util.stream.Collectors.toList());
        return success(list);
    }

    @Operation(summary = "获取外卖跑腿待接单订单列表")
    @ApiOperationSupport(order = 190)
    @GetMapping("/takeoutList")
    public AjaxResult takeoutOrderList(@RequestParam(value = "communityId", required = false) Long communityId) {
        OrderMain query = new OrderMain();
        query.setOrderType(1);
        query.setOrderStatus(1);
        query.setCommunityId(communityId);
        java.util.List<OrderVO> list = orderService.selectOrderMainList(query)
                .stream()
                .filter(o -> o.getRunnerId() == null)
                .map(this::toOrderVOWithoutItems)
                .collect(java.util.stream.Collectors.toList());
        return success(list);
    }

    @Operation(summary = "获取超市跑腿待接单订单列表")
    @ApiOperationSupport(order = 200)
    @GetMapping("/supermarketList")
    public AjaxResult supermarketOrderList(@RequestParam(value = "communityId", required = false) Long communityId) {
        OrderMain query = new OrderMain();
        query.setOrderType(2);
        query.setOrderStatus(1);
        query.setCommunityId(communityId);
        java.util.List<OrderVO> list = orderService.selectOrderMainList(query)
                .stream()
                .filter(o -> o.getRunnerId() == null)
                .map(this::toOrderVOWithoutItems)
                .collect(java.util.stream.Collectors.toList());
        return success(list);
    }

    @Autowired
    private ITakeawayStoreService storeService;

    @Operation(summary = "获取订单详情")
    @ApiOperationSupport(order = 210)
    @GetMapping("/detail/{id}")
    public AjaxResult getOrderDetail(HttpServletRequest request, @PathVariable("id") Long id) {
        MiniLoginUser loginUser = tokenService.getLoginUser(request);
        if (loginUser == null) {
            return error("未登录");
        }
        OrderMain order = orderService.selectOrderMainById(id);
        if (order == null) {
            return error("订单不存在");
        }
        if (!loginUser.getUserId().equals(order.getUserId()) && !loginUser.getUserId().equals(order.getRunnerId())) {
            return error("无权查看");
        }
        
        OrderVO vo = toOrderVO(order);
        
        // Fetch items
        java.util.List<OrderItem> items = orderService.selectOrderItemsByOrderId(id);
        if (items != null && !items.isEmpty()) {
            java.util.List<com.axin.system.domain.vo.OrderItemVO> itemVOs = items.stream().map(it -> {
                com.axin.system.domain.vo.OrderItemVO iv = new com.axin.system.domain.vo.OrderItemVO();
                iv.setId(it.getId());
                iv.setItemType(it.getItemType());
                iv.setPackageId(it.getPackageId());
                iv.setProductId(it.getProductId());
                iv.setProductName(it.getProductName());
                iv.setQuantity(it.getQuantity());
                iv.setUnitPrice(it.getUnitPrice());
                iv.setSubtotal(it.getSubtotal());
                if (it.getStoreId() != null) {
                    TakeawayStore s = storeService.selectTakeawayStoreById(it.getStoreId());
                    if (s != null) iv.setStoreName(s.getStoreName());
                }
                return iv;
            }).collect(java.util.stream.Collectors.toList());
            vo.setItems(itemVOs);
        }
        
        return success(vo);
    }

    private OrderVO toOrderVO(OrderMain e) {
        if (e == null) return null;
        OrderVO vo = new OrderVO();
        vo.setId(e.getId());
        vo.setOrderNo(e.getOrderNo());
        vo.setUserId(e.getUserId());
        vo.setRunnerId(e.getRunnerId());
        vo.setCommunityId(e.getCommunityId());
        vo.setDetailAddress(e.getDetailAddress());
        vo.setOrderType(e.getOrderType());
        vo.setServiceType(e.getServiceType());
        vo.setStartAddress(e.getStartAddress());
        vo.setEndAddress(e.getEndAddress());
        vo.setDeliveryAddress(e.getDeliveryAddress());
        vo.setItemDesc(e.getItemDesc());
        vo.setBudget(e.getBudget());
        vo.setSpecName(e.getSpecName());
        vo.setWechatId(e.getWechatId());
        vo.setDurationType(e.getDurationType());
        vo.setDurationValue(e.getDurationValue());
        vo.setTotalAmount(e.getTotalAmount());
        vo.setDiscountAmount(e.getDiscountAmount());
        vo.setActualAmount(e.getActualAmount());
        vo.setCommissionAmount(e.getCommissionAmount());
        vo.setOrderStatus(e.getOrderStatus());
        vo.setPayStatus(e.getPayStatus());
        vo.setPayMethod(e.getPayMethod());
        vo.setPayTime(e.getPayTime());
        vo.setFinishTime(e.getFinishTime());
        vo.setCancelReason(e.getCancelReason());
        vo.setNote(e.getNote());
        vo.setCreateTime(e.getCreateTime());
        vo.setUpdateTime(e.getUpdateTime());
        WechatUser u = e.getUserId() == null ? null : wechatUserService.selectWechatUserById(e.getUserId());
        if (u != null) {
            vo.setUserNickname(u.getNickname());
        }
        if (e.getRunnerId() != null) {
            WechatUser r = wechatUserService.selectWechatUserById(e.getRunnerId());
            if (r != null) {
                vo.setRunnerNickname(r.getNickname());
            }
        }
        Long cId = u != null && u.getCommunityId() != null ? u.getCommunityId() : e.getCommunityId();
        if (cId != null) {
            Community c = communityService.selectCommunityById(cId);
            if (c != null) {
                vo.setCommunityName(c.getCommunityName());
            }
        }
        return vo;
    }

    private OrderVO toOrderVOWithoutItems(OrderMain e) {
        return toOrderVO(e);
    }

    private String genOrderNo(Integer type) {
        String t = new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        int r = 100 + new java.util.Random().nextInt(900);
        String prefix = "OT";
        if (type != null) {
            if (type == 1) prefix = "OM";
            if (type == 2) prefix = "SM";
        }
        return prefix + "-" + t + r;
    }

    
}
