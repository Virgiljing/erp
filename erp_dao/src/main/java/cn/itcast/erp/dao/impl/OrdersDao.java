package cn.itcast.erp.dao.impl;

import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.entity.Orders;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 * 订单数据访问类
 *
 */
@Repository("ordersDao")
public class OrdersDao extends BaseDao<Orders> implements IOrdersDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Orders orders1,Orders orders2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Orders.class);
        if(orders1!=null){
            if(!StringUtils.isEmpty(orders1.getType())){
                dc.add(Restrictions.eq("type", orders1.getType()));
            }
            if(!StringUtils.isEmpty(orders1.getState())){
                dc.add(Restrictions.eq("state", orders1.getState()));
            }
            // 下单员
            if(null != orders1.getCreater()) {
                dc.add(Restrictions.eq("creater", orders1.getCreater()));
            }

        }
        return dc;
    }

}
