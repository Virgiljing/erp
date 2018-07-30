package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IReturnorderdetailDao;
import cn.itcast.erp.entity.Returnorderdetail;

/**
 * 退货订单明细数据访问类
 *
 */
@Repository("returnorderdetailDao")
public class ReturnorderdetailDao extends BaseDao<Returnorderdetail> implements IReturnorderdetailDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Returnorderdetail returnorderdetail1,Returnorderdetail returnorderdetail2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Returnorderdetail.class);
        if(returnorderdetail1!=null){
            if(!StringUtils.isEmpty(returnorderdetail1.getGoodsname())){
                dc.add(Restrictions.like("goodsname", returnorderdetail1.getGoodsname(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(returnorderdetail1.getState())){
                dc.add(Restrictions.like("state", returnorderdetail1.getState(), MatchMode.ANYWHERE));
            }
            //退货订单查询
            if(null != returnorderdetail1.getReturnorders() && returnorderdetail1.getReturnorders().getUuid() != null) {
                dc.add(Restrictions.eq("returnorders",returnorderdetail1.getReturnorders()));
            }
          

        }
        return dc;
    }
}
