package cn.itcast.erp.biz;

import cn.itcast.erp.entity.Returnorders;

/**
 * 退货订单业务逻辑层接口
 */
public interface IReturnordersBiz extends IBaseBiz<Returnorders> {
    /**
     * 采购退货订单登记
     * @param returnorders
     */
    void add(Returnorders returnorders);

    /**
     * 审核销售退货订单
     *
     * @param id    审核订单的编号
     * @param uuiid 当前操作用户的编号
     * @return
     */
    void doCheck(Long uuid, Long id);

    /**
     * 采购退货审核
     *
     * @param logUserUuid
     * @param id
     */
    void type1outGoodsDoCheck(Long logUserUuid, Long id);


    /**
     * 销售退单新增登记
     * (赵萌)
     *
     * @param returnorders
     */
    void addSaleReturnOrder(Returnorders returnorders);
}

