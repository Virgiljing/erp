package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;

/**
 * 供应商数据访问类
 *
 */
@Repository("supplierDao")
public class SupplierDao extends BaseDao<Supplier> implements ISupplierDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Supplier supplier1,Supplier supplier2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Supplier.class);
        if(supplier1!=null){
            if(!StringUtils.isEmpty(supplier1.getName())){
                dc.add(Restrictions.like("name", supplier1.getName(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(supplier1.getAddress())){
                dc.add(Restrictions.like("address", supplier1.getAddress(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(supplier1.getContact())){
                dc.add(Restrictions.like("contact", supplier1.getContact(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(supplier1.getTele())){
                dc.add(Restrictions.like("tele", supplier1.getTele(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(supplier1.getEmail())){
                dc.add(Restrictions.like("email", supplier1.getEmail(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(supplier1.getType())){
                dc.add(Restrictions.eq("type", supplier1.getType()));
            }
        }
        if(null != supplier2) {
            // 按名称精确查询
            if(!StringUtils.isEmpty(supplier2.getName())){
                dc.add(Restrictions.eq("name", supplier2.getName()));
            }
        }
        return dc;
    }
}
