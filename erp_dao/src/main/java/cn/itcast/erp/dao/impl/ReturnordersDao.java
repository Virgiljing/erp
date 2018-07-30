package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IReturnordersDao;
import cn.itcast.erp.entity.Returnorders;

/**
 * 退货订单数据访问类
 *
 */
@Repository("returnordersDao")
public class ReturnordersDao extends BaseDao<Returnorders> implements IReturnordersDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Returnorders returnorders1,Returnorders returnorders2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Returnorders.class);
        if(returnorders1!=null){
            if(!StringUtils.isEmpty(returnorders1.getType())){
                dc.add(Restrictions.eq("type", returnorders1.getType()));
            }//修改like为eq
            if(!StringUtils.isEmpty(returnorders1.getState())){
                dc.add(Restrictions.eq("state", returnorders1.getState()));
            }//修改like为eq

        }
        return dc;
    }
}
