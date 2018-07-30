package cn.itcast.erp.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ParameterMode;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.procedure.ProcedureOutputs;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.entity.Orderdetail;

/**
 * 订单明细数据访问类
 *
 */
@Repository("orderdetailDao")
public class OrderdetailDao extends BaseDao<Orderdetail> implements IOrderdetailDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Orderdetail orderdetail1,Orderdetail orderdetail2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Orderdetail.class);
        if(orderdetail1!=null){
            if(!StringUtils.isEmpty(orderdetail1.getGoodsname())){
                dc.add(Restrictions.like("goodsname", orderdetail1.getGoodsname(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(orderdetail1.getState())){
                dc.add(Restrictions.eq("state", orderdetail1.getState()));
            }
            // 订单
            if(null != orderdetail1.getOrders() && orderdetail1.getOrders().getUuid() != null) {
                dc.add(Restrictions.eq("orders", orderdetail1.getOrders()));
            }
        }
        return dc;
    }

    @Override
    public Map<String, Object> doOutStoreSPC(final Long empuuid, final Long storeuuid, final Long uuid) {

        return this.getHibernateTemplate().executeWithNativeSession(new HibernateCallback<Map<String, Object>>(){

            @Override
            public Map<String, Object> doInHibernate(Session session) throws HibernateException {
                ProcedureCall call = session.createStoredProcedureCall("proc_doOutStore");// 调用存储过程

                // 下标从1开始
                call.registerParameter(1, long.class, ParameterMode.IN).bindValue(empuuid); // 设置输入参数和值
                call.registerParameter(2, long.class, ParameterMode.IN).bindValue(storeuuid);
                call.registerParameter(3, long.class, ParameterMode.IN).bindValue(uuid);
                call.registerParameter(4, long.class, ParameterMode.OUT);
                call.registerParameter(5, String.class, ParameterMode.OUT);
                ProcedureOutputs outputs = call.getOutputs();// 执行过程
                Map<String, Object> result = new HashMap<String, Object>();
                // 获取输出参数的值
                Long outFlag = (Long)outputs.getOutputParameterValue(4);
                result.put("success", outFlag==0);
                result.put("message", outputs.getOutputParameterValue(5));
                return result;
            }

        });
    }
}
