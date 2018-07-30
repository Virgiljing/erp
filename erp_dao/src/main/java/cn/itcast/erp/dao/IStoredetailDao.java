package cn.itcast.erp.dao;

import java.io.Serializable;
import java.util.List;

import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;

/**
 * 仓库库存数据访问接口
 *
 */
public interface IStoredetailDao extends IBaseDao<Storedetail>{
    /**
     * 获取库存预警列表
     * @return
     */
    List<Storealert> getStorealertList();

    /**
     *  查询库存数量
     * @param goodsuuid
     * @return
     */
    Long findStoreNumByGoodsuuidNotStoreuuid(Long goodsuuid);
    /**
     * 通过仓库编号和商品编号查询库存明细
     * @param uuid
     * @param goodsid
     * @return
     */
    Storedetail getStoredetail(Serializable uuid,Long goodsid);
}
