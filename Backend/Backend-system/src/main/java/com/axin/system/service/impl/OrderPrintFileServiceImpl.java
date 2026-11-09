package com.axin.system.service.impl;

import java.util.List;
import com.axin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.axin.system.mapper.OrderPrintFileMapper;
import com.axin.system.domain.OrderPrintFile;
import com.axin.system.service.IOrderPrintFileService;

/**
 * 订单打印文件Service业务层处理
 * 
 * @author axin
 * @date 2023-10-27
 */
@Service
public class OrderPrintFileServiceImpl implements IOrderPrintFileService 
{
    @Autowired
    private OrderPrintFileMapper orderPrintFileMapper;

    /**
     * 查询订单打印文件
     * 
     * @param id 订单打印文件主键
     * @return 订单打印文件
     */
    @Override
    public OrderPrintFile selectOrderPrintFileById(Long id)
    {
        return orderPrintFileMapper.selectOrderPrintFileById(id);
    }

    /**
     * 查询订单打印文件列表
     * 
     * @param orderPrintFile 订单打印文件
     * @return 订单打印文件
     */
    @Override
    public List<OrderPrintFile> selectOrderPrintFileList(OrderPrintFile orderPrintFile)
    {
        return orderPrintFileMapper.selectOrderPrintFileList(orderPrintFile);
    }

    /**
     * 新增订单打印文件
     * 
     * @param orderPrintFile 订单打印文件
     * @return 结果
     */
    @Override
    public int insertOrderPrintFile(OrderPrintFile orderPrintFile)
    {
        orderPrintFile.setCreateTime(DateUtils.getNowDate());
        return orderPrintFileMapper.insertOrderPrintFile(orderPrintFile);
    }

    /**
     * 修改订单打印文件
     * 
     * @param orderPrintFile 订单打印文件
     * @return 结果
     */
    @Override
    public int updateOrderPrintFile(OrderPrintFile orderPrintFile)
    {
        return orderPrintFileMapper.updateOrderPrintFile(orderPrintFile);
    }

    /**
     * 批量删除订单打印文件
     * 
     * @param ids 需要删除的订单打印文件主键
     * @return 结果
     */
    @Override
    public int deleteOrderPrintFileByIds(Long[] ids)
    {
        return orderPrintFileMapper.deleteOrderPrintFileByIds(ids);
    }

    /**
     * 删除订单打印文件信息
     * 
     * @param id 订单打印文件主键
     * @return 结果
     */
    @Override
    public int deleteOrderPrintFileById(Long id)
    {
        return orderPrintFileMapper.deleteOrderPrintFileById(id);
    }
}
