package cn.itcast.erp.biz;

import cn.itcast.erp.entity.Orders;

import java.io.IOException;
import java.io.OutputStream;
/**
 * 订单业务逻辑层接口
 *
 */
public interface IOrdersBiz extends IBaseBiz<Orders>{

    /**
     * 订单的审核
     * @param empuuid
     * @param uuid
     */
    void doCheck(Long empuuid, Long uuid);

    /**
     * 订单的确认
     * @param empuuid
     * @param uuid
     */
    void doStart(Long empuuid, Long uuid);

    /**
     * 导出订单
     * @param os
     * @param uuid
     * @throws IOException
     */
    void exportById(OutputStream os, Long uuid) throws IOException;

}

