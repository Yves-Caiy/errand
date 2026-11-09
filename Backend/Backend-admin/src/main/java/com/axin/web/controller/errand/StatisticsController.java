package com.axin.web.controller.errand;

import com.axin.common.core.controller.BaseController;
import com.axin.common.core.domain.AjaxResult;
import com.axin.system.domain.OrderMain;
import com.axin.system.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Validated
@Tag(name = "后台数据看台", description = "订单统计接口")
@RestController
@RequestMapping("/errand/stats")
public class StatisticsController extends BaseController {

    @Autowired
    private IOrderService orderService;

    @Operation(summary = "订单统计总览", description = "统计订单总数、进行中(2)、已完成(3)、已取消(4)、收入金额(支付=1且完成=3)，按全部、今日、昨日、本月返回")
    @PreAuthorize("@ss.hasPermi('errand:order:list')")
    @GetMapping("/overview")
    public AjaxResult overview() {
        List<OrderMain> all = orderService.selectOrderMainList(new OrderMain());
        StatsDTO total = calc(all, null, null);
        StatsDTO today = calc(all, startOfToday(), startOfTomorrow());
        StatsDTO yesterday = calc(all, startOfYesterday(), startOfToday());
        StatsDTO month = calc(all, startOfMonth(), startOfTomorrow());
        StatsOverview vo = new StatsOverview();
        vo.setTotal(total);
        vo.setToday(today);
        vo.setYesterday(yesterday);
        vo.setMonth(month);
        return success(vo);
    }

    @Operation(summary = "按类型订单统计", description = "按order_type分别统计：订单总数、总收入、月收入、日收入、总退款、月退款、日退款")
    @PreAuthorize("@ss.hasPermi('errand:order:list')")
    @GetMapping("/type")
    public AjaxResult typeOverview() {
        List<OrderMain> orders = orderService.selectOrderMainList(new OrderMain());
        java.util.Map<Integer, TypeStatsDTO> map = new java.util.HashMap<>();
        LocalDateTime todayStart = startOfToday();
        LocalDateTime tomorrowStart = startOfTomorrow();
        LocalDateTime monthStart = startOfMonth();
        ZoneId zone = ZoneId.systemDefault();
        for (OrderMain e : orders) {
            Integer type = e.getOrderType();
            if (type == null) continue;
            TypeStatsDTO dto = map.computeIfAbsent(type, k -> {
                TypeStatsDTO t = new TypeStatsDTO();
                t.setOrderType(k);
                return t;
            });
            dto.setCount(dto.getCount() + 1);
            Integer payStatus = e.getPayStatus();
            Integer orderStatus = e.getOrderStatus();
            if (payStatus != null && orderStatus != null && payStatus == 1 && orderStatus == 3) {
                Long amt = e.getActualAmount();
                if (amt != null) {
                    dto.setRevenueTotal(dto.getRevenueTotal() + amt);
                    Date pt = e.getPayTime();
                    if (pt != null) {
                        LocalDateTime t = LocalDateTime.ofInstant(pt.toInstant(), zone);
                        if (!t.isBefore(monthStart) && t.isBefore(tomorrowStart)) {
                            dto.setRevenueMonth(dto.getRevenueMonth() + amt);
                        }
                        if (!t.isBefore(todayStart) && t.isBefore(tomorrowStart)) {
                            dto.setRevenueToday(dto.getRevenueToday() + amt);
                        }
                    }
                }
            }
            if (payStatus != null && payStatus == 3) {
                Long amt = e.getActualAmount();
                if (amt != null) {
                    dto.setRefundTotal(dto.getRefundTotal() + amt);
                    Date rt = e.getUpdateTime() != null ? e.getUpdateTime() : e.getPayTime();
                    if (rt != null) {
                        LocalDateTime t = LocalDateTime.ofInstant(rt.toInstant(), zone);
                        if (!t.isBefore(monthStart) && t.isBefore(tomorrowStart)) {
                            dto.setRefundMonth(dto.getRefundMonth() + amt);
                        }
                        if (!t.isBefore(todayStart) && t.isBefore(tomorrowStart)) {
                            dto.setRefundToday(dto.getRefundToday() + amt);
                        }
                    }
                }
            }
        }
        java.util.List<TypeStatsDTO> list = new java.util.ArrayList<>(map.values());
        list.sort(java.util.Comparator.comparing(TypeStatsDTO::getOrderType));
        return success(list);
    }

    @Operation(summary = "订单量趋势", description = "range=today|7|30，返回x轴与y值，订单量按创建时间统计")
    @PreAuthorize("@ss.hasPermi('errand:order:list')")
    @GetMapping("/orderTrend")
    public AjaxResult orderTrend(@RequestParam(value = "range", required = false, defaultValue = "today") String range) {
        List<OrderMain> orders = orderService.selectOrderMainList(new OrderMain());
        TrendDTO dto = buildTrend(range, orders, false);
        return success(dto);
    }

    @Operation(summary = "收入趋势", description = "range=today|7|30，返回x轴与y值，收入按支付时间统计（pay_status=1且order_status=3，金额actualAmount）")
    @PreAuthorize("@ss.hasPermi('errand:order:list')")
    @GetMapping("/revenueTrend")
    public AjaxResult revenueTrend(@RequestParam(value = "range", required = false, defaultValue = "today") String range) {
        List<OrderMain> orders = orderService.selectOrderMainList(new OrderMain());
        TrendDTO dto = buildTrend(range, orders, true);
        return success(dto);
    }

    @Operation(summary = "订单状态分布", description = "按全部订单统计进行中(2)/已完成(3)/已取消(4)分布")
    @PreAuthorize("@ss.hasPermi('errand:order:list')")
    @GetMapping("/statusDist")
    public AjaxResult statusDist() {
        List<OrderMain> orders = orderService.selectOrderMainList(new OrderMain());
        long progressing = 0;
        long completed = 0;
        long cancelled = 0;
        for (OrderMain e : orders) {
            Integer st = e.getOrderStatus();
            if (st == null) continue;
            if (st == 2) progressing++;
            else if (st == 3) completed++;
            else if (st == 4) cancelled++;
        }
        java.util.List<DistItemDTO> data = new java.util.ArrayList<>();
        DistItemDTO d1 = new DistItemDTO();
        d1.setName("进行中");
        d1.setValue(progressing);
        data.add(d1);
        DistItemDTO d2 = new DistItemDTO();
        d2.setName("已完成");
        d2.setValue(completed);
        data.add(d2);
        DistItemDTO d3 = new DistItemDTO();
        d3.setName("已取消");
        d3.setValue(cancelled);
        data.add(d3);
        return success(data);
    }

    @Operation(summary = "订单类型分布", description = "按全部订单统计订单类型分布：1外卖、2超市、3套餐、4服务")
    @PreAuthorize("@ss.hasPermi('errand:order:list')")
    @GetMapping("/typeDist")
    public AjaxResult typeDist() {
        List<OrderMain> orders = orderService.selectOrderMainList(new OrderMain());
        long c1 = 0;
        long c2 = 0;
        long c3 = 0;
        long c4 = 0;
        for (OrderMain e : orders) {
            Integer t = e.getOrderType();
            if (t == null) continue;
            if (t == 1) c1++;
            else if (t == 2) c2++;
            else if (t == 3) c3++;
            else if (t == 4) c4++;
        }
        java.util.List<DistItemDTO> data = new java.util.ArrayList<>();
        DistItemDTO d1 = new DistItemDTO();
        d1.setName("外卖订单");
        d1.setValue(c1);
        data.add(d1);
        DistItemDTO d2 = new DistItemDTO();
        d2.setName("超市订单");
        d2.setValue(c2);
        data.add(d2);
        DistItemDTO d3 = new DistItemDTO();
        d3.setName("套餐订单");
        d3.setValue(c3);
        data.add(d3);
        DistItemDTO d4 = new DistItemDTO();
        d4.setName("服务订单");
        d4.setValue(c4);
        data.add(d4);
        return success(data);
    }

    private StatsDTO calc(List<OrderMain> orders, LocalDateTime start, LocalDateTime end) {
        StatsDTO s = new StatsDTO();
        ZoneId zone = ZoneId.systemDefault();
        for (OrderMain e : orders) {
            Date ct = e.getCreateTime();
            boolean countInRange = inRange(ct, start, end, zone);
            if (start == null && end == null) {
                countInRange = true;
            }
            if (countInRange) {
                s.setOrders(s.getOrders() + 1);
                Integer st = e.getOrderStatus();
                if (st != null) {
                    if (st == 2) s.setProgressing(s.getProgressing() + 1);
                    else if (st == 3) s.setCompleted(s.getCompleted() + 1);
                    else if (st == 4) s.setCancelled(s.getCancelled() + 1);
                }
            }
            Integer payStatus = e.getPayStatus();
            Integer orderStatus = e.getOrderStatus();
            if (payStatus != null && orderStatus != null && payStatus == 1 && orderStatus == 3) {
                Date pt = e.getPayTime();
                boolean revenueInRange = inRange(pt, start, end, zone);
                if ((start == null && end == null) || revenueInRange) {
                    Long amt = e.getActualAmount();
                    if (amt != null) s.setRevenue(s.getRevenue() + amt);
                }
            }
        }
        return s;
    }

    private boolean inRange(Date d, LocalDateTime start, LocalDateTime end, ZoneId zone) {
        if (d == null) return false;
        if (start == null && end == null) return true;
        LocalDateTime t = LocalDateTime.ofInstant(d.toInstant(), zone);
        boolean ge = start == null || !t.isBefore(start);
        boolean lt = end == null || t.isBefore(end);
        return ge && lt;
    }

    private LocalDateTime startOfToday() {
        return LocalDate.now().atStartOfDay();
    }

    private LocalDateTime startOfTomorrow() {
        return LocalDate.now().plusDays(1).atStartOfDay();
    }

    private LocalDateTime startOfYesterday() {
        return LocalDate.now().minusDays(1).atStartOfDay();
    }

    private LocalDateTime startOfMonth() {
        return LocalDate.now().withDayOfMonth(1).atStartOfDay();
    }

    private TrendDTO buildTrend(String range, List<OrderMain> orders, boolean revenueMode) {
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime start;
        LocalDateTime end = startOfTomorrow();
        java.util.List<LocalDateTime> bucketStarts = new java.util.ArrayList<>();
        java.util.List<LocalDateTime> bucketEnds = new java.util.ArrayList<>();
        java.util.List<String> labels = new java.util.ArrayList<>();
        DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("MM-dd");
        if ("today".equalsIgnoreCase(range)) {
            start = startOfToday();
            for (int i = 0; i < 24; i++) {
                LocalDateTime s = start.plusHours(i);
                bucketStarts.add(s);
                bucketEnds.add(s.plusHours(1));
                labels.add(String.format("%02d", i));
            }
        } else if ("7".equalsIgnoreCase(range)) {
            start = startOfToday().minusDays(6);
            for (int i = 0; i < 7; i++) {
                LocalDateTime s = start.plusDays(i);
                bucketStarts.add(s);
                bucketEnds.add(s.plusDays(1));
                labels.add(s.format(dayFmt));
            }
        } else {
            start = startOfToday().minusDays(29);
            for (int i = 0; i < 30; i++) {
                LocalDateTime s = start.plusDays(i);
                bucketStarts.add(s);
                bucketEnds.add(s.plusDays(1));
                labels.add(s.format(dayFmt));
            }
        }
        int n = bucketStarts.size();
        long[] y = new long[n];
        for (OrderMain e : orders) {
            Date dt;
            long val = 1;
            if (revenueMode) {
                Integer payStatus = e.getPayStatus();
                Integer orderStatus = e.getOrderStatus();
                if (payStatus == null || orderStatus == null || payStatus != 1 || orderStatus != 3) {
                    continue;
                }
                dt = e.getPayTime();
                if (dt == null) continue;
                Long amt = e.getActualAmount();
                val = amt == null ? 0 : amt;
            } else {
                dt = e.getCreateTime();
                if (dt == null) continue;
            }
            LocalDateTime t = LocalDateTime.ofInstant(dt.toInstant(), zone);
            for (int i = 0; i < n; i++) {
                LocalDateTime s = bucketStarts.get(i);
                LocalDateTime ee = bucketEnds.get(i);
                if ((t.isEqual(s) || t.isAfter(s)) && t.isBefore(ee)) {
                    y[i] += val;
                    break;
                }
            }
        }
        TrendDTO dto = new TrendDTO();
        dto.setX(labels);
        java.util.List<Long> ys = new java.util.ArrayList<>(n);
        for (int i = 0; i < n; i++) ys.add(y[i]);
        dto.setY(ys);
        return dto;
    }

    public static class StatsDTO {
        private int orders;
        private int progressing;
        private int completed;
        private int cancelled;
        private long revenue;

        public int getOrders() {
            return orders;
        }

        public void setOrders(int orders) {
            this.orders = orders;
        }

        public int getProgressing() {
            return progressing;
        }

        public void setProgressing(int progressing) {
            this.progressing = progressing;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        public int getCancelled() {
            return cancelled;
        }

        public void setCancelled(int cancelled) {
            this.cancelled = cancelled;
        }

        public long getRevenue() {
            return revenue;
        }

        public void setRevenue(long revenue) {
            this.revenue = revenue;
        }
    }

    public static class StatsOverview {
        private StatsDTO total;
        private StatsDTO today;
        private StatsDTO yesterday;
        private StatsDTO month;

        public StatsDTO getTotal() {
            return total;
        }

        public void setTotal(StatsDTO total) {
            this.total = total;
        }

        public StatsDTO getToday() {
            return today;
        }

        public void setToday(StatsDTO today) {
            this.today = today;
        }

        public StatsDTO getYesterday() {
            return yesterday;
        }

        public void setYesterday(StatsDTO yesterday) {
            this.yesterday = yesterday;
        }

        public StatsDTO getMonth() {
            return month;
        }

        public void setMonth(StatsDTO month) {
            this.month = month;
        }
    }

    public static class TrendDTO {
        private java.util.List<String> x;
        private java.util.List<Long> y;

        public java.util.List<String> getX() {
            return x;
        }

        public void setX(java.util.List<String> x) {
            this.x = x;
        }

        public java.util.List<Long> getY() {
            return y;
        }

        public void setY(java.util.List<Long> y) {
            this.y = y;
        }
    }

    public static class TypeStatsDTO {
        private Integer orderType;
        private int count;
        private long revenueTotal;
        private long revenueMonth;
        private long revenueToday;
        private long refundTotal;
        private long refundMonth;
        private long refundToday;

        public Integer getOrderType() {
            return orderType;
        }

        public void setOrderType(Integer orderType) {
            this.orderType = orderType;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getRevenueTotal() {
            return revenueTotal;
        }

        public void setRevenueTotal(long revenueTotal) {
            this.revenueTotal = revenueTotal;
        }

        public long getRevenueMonth() {
            return revenueMonth;
        }

        public void setRevenueMonth(long revenueMonth) {
            this.revenueMonth = revenueMonth;
        }

        public long getRevenueToday() {
            return revenueToday;
        }

        public void setRevenueToday(long revenueToday) {
            this.revenueToday = revenueToday;
        }

        public long getRefundTotal() {
            return refundTotal;
        }

        public void setRefundTotal(long refundTotal) {
            this.refundTotal = refundTotal;
        }

        public long getRefundMonth() {
            return refundMonth;
        }

        public void setRefundMonth(long refundMonth) {
            this.refundMonth = refundMonth;
        }

        public long getRefundToday() {
            return refundToday;
        }

        public void setRefundToday(long refundToday) {
            this.refundToday = refundToday;
        }
    }

    public static class DistItemDTO {
        private String name;
        private long value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }
    }
}
