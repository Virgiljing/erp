package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.entity.Store;

/**
 * 仓库数据访问类
 *
 */
@Repository("storeDao")
public class StoreDao extends BaseDao<Store> implements IStoreDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Store store1,Store store2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Store.class);
        if(store1!=null){
            if(!StringUtils.isEmpty(store1.getName())){
                dc.add(Restrictions.like("name", store1.getName(), MatchMode.ANYWHERE));
            }
            // 按库管员查询
            if(null != store1.getEmpuuid()) {
                dc.add(Restrictions.eq("empuuid", store1.getEmpuuid()));
            }

        }
        return dc;
    }
}
