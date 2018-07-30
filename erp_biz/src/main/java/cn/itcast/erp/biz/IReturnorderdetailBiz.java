package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Returnorderdetail;
/**
 * 退货订单明细业务逻辑层接口
 *
 */
public interface IReturnorderdetailBiz extends IBaseBiz<Returnorderdetail>{
	/**
     * 退货出库业务   	
     * @param empuuid	操作员工,库管员
     * @param storeuuid	仓库编号
     * @param uuid	明细编号
     */
    void returnOrderOutStore(Long empuuid, Long storeuuid, Long uuid);
    /**
     * 销售退货入库
     */
	void doInStore(Long uuid, Long storeuuid, Long id);
}

