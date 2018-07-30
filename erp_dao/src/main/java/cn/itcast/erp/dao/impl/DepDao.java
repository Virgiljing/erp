package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IDepDao;
import cn.itcast.erp.entity.Dep;

/**
 * 部门数据访问类
 *
 */
@Repository("depDao")
public class DepDao extends BaseDao<Dep> implements IDepDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Dep dep1,Dep dep2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Dep.class);
        if(dep1!=null){
            if(!StringUtils.isEmpty(dep1.getName())){  //根据部门名称查找部门
                dc.add(Restrictions.like("name", dep1.getName(),MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(dep1.getTele())){
                dc.add(Restrictions.like("tele", dep1.getTele(), MatchMode.ANYWHERE));
            }
        }
        if(dep2!=null){
        	if(!StringUtils.isEmpty(dep2.getName())){  //根据部门名称查找部门
        		dc.add(Restrictions.eq("name", dep2.getName()));
        	}
        }
        return dc;
    }
}
