package cn.itcast.erp.biz;
import java.util.Map;

import cn.itcast.erp.entity.Orderdetail;
/**
 * 订单明细业务逻辑层接口
 *
 */
public interface IOrderdetailBiz extends IBaseBiz<Orderdetail>{

    /**
     * 入库业务
     * @param empuuid   操作员工，库管员
     * @param storeuuid 仓库编号
     * @param uuid      明细编号
     */
    void doInStore(Long empuuid, Long storeuuid, Long uuid);

    /**
     * 出业务
     * @param empuuid   操作员工，库管员
     * @param storeuuid 仓库编号
     * @param uuid      明细编号
     */
    void doOutStore(Long empuuid, Long storeuuid, Long uuid);
    
    Map<String,Object> doOutStoreSPC(Long empuuid, Long storeuuid, Long uuid);
}

