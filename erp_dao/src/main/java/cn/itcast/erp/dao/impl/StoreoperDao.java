package cn.itcast.erp.dao.impl;

import java.util.Calendar;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Storeoper;

/**
 * 仓库操作记录数据访问类
 *
 */
@Repository("storeoperDao")
public class StoreoperDao extends BaseDao<Storeoper> implements IStoreoperDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Storeoper storeoper1,Storeoper storeoper2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Storeoper.class);
        if(storeoper1!=null){
            if(!StringUtils.isEmpty(storeoper1.getType())){
                dc.add(Restrictions.eq("type", storeoper1.getType()));
            }
            // 员工
            if(null != storeoper1.getEmpuuid()) {
                dc.add(Restrictions.eq("empuuid", storeoper1.getEmpuuid()));
            }
            // 仓库
            if(null != storeoper1.getStoreuuid()) {
                dc.add(Restrictions.eq("storeuuid", storeoper1.getStoreuuid()));
            }
            // 商品
            if(null != storeoper1.getGoodsuuid()) {
                dc.add(Restrictions.eq("goodsuuid", storeoper1.getGoodsuuid()));
            }
            // 开始时间 >=
            if(null != storeoper1.getOpertime()) {
                dc.add(Restrictions.ge("opertime", storeoper1.getOpertime()));
            }
        }
        if(storeoper2!=null){
            // 结束时间 <=
            if(null != storeoper2.getOpertime()) {
                Calendar car = Calendar.getInstance();
                car.setTime(storeoper2.getOpertime());// 设置要操作的时间对象
                //yyyy-MM-dd HH:mm:ss
                car.set(Calendar.HOUR, 23);
                car.set(Calendar.MINUTE, 59);
                car.set(Calendar.SECOND, 59);
                car.set(Calendar.MILLISECOND, 999);
                //car.add(Calendar.DATE, 1);// 自动跨年跨月
                dc.add(Restrictions.le("opertime", car.getTime()));
            }
        }
        return dc;
    }
}
