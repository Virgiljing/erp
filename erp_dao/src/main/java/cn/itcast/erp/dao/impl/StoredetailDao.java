package cn.itcast.erp.dao.impl;

import java.io.Serializable;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;

/**
 * 仓库库存数据访问类
 *
 */
@Repository("storedetailDao")
@SuppressWarnings("unchecked")
public class StoredetailDao extends BaseDao<Storedetail> implements IStoredetailDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Storedetail sd1,Storedetail sd2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Storedetail.class);
        if(sd1!=null){
            // 仓库编号
            if(null != sd1.getStoreuuid()) {
                dc.add(Restrictions.eq("storeuuid", sd1.getStoreuuid()));
            }
            // 商品编号
            if(null != sd1.getGoodsuuid()) {
                dc.add(Restrictions.eq("goodsuuid", sd1.getGoodsuuid()));
            }
        }
        return dc;
    }

    @Override
    public List<Storealert> getStorealertList() {
        String hql = "from Storealert where storenum < outnum";
        return (List<Storealert>) this.getHibernateTemplate().find(hql);
    }

    /*
     * 通过商品编号和仓库编号查询库存明细
     * uuid 仓库编号
     * goodsid 商品编号
     */
    public Storedetail getStoredetail(Serializable uuid,Long goodsid) {
    	 List<Storedetail> list = (List<Storedetail>) this.getHibernateTemplate().find("from Storedetail where storeuuid = ? and goodsuuid = ?", uuid,goodsid);

    	return list.get(0);
    }

    @Override
    public Long findStoreNumByGoodsuuidNotStoreuuid(Long goodsuuid) {
        List<Long> list = (List<Long>) this.getHibernateTemplate().find("select sum(num) from Storedetail " +
                "where goodsuuid = ?", goodsuuid);
        return list.get(0);
    }
}
