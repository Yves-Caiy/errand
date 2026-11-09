package com.axin.system.mapper;

import java.util.List;
import com.axin.system.domain.OrderPrintFile;

/**
 * 订单打印文件Mapper接口
 * 
 * @author axin
 * @date 2023-10-27
 */
public interface OrderPrintFileMapper 
{
    /**
     * 查询订单打印文件
     * 
     * @param id 订单打印文件主键
     * @return 订单打印文件
     */
    public OrderPrintFile selectOrderPrintFileById(Long id);

    /**
     * 查询订单打印文件列表
     * 
     * @param orderPrintFile 订单打印文件
     * @return 订单打印文件集合
     */
    public List<OrderPrintFile> selectOrderPrintFileList(OrderPrintFile orderPrintFile);

    /**
     * 新增订单打印文件
     * 
     * @param orderPrintFile 订单打印文件
     * @return 结果
     */
    public int insertOrderPrintFile(OrderPrintFile orderPrintFile);

    /**
     * 修改订单打印文件
     * 
     * @param orderPrintFile 订单打印文件
     * @return 结果
     */
    public int updateOrderPrintFile(OrderPrintFile orderPrintFile);

    /**
     * 删除订单打印文件
     * 
     * @param id 订单打印文件主键
     * @return 结果
     */
    public int deleteOrderPrintFileById(Long id);

    /**
     * 批量删除订单打印文件
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOrderPrintFileByIds(Long[] ids);
}
