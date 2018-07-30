package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Role;

/**
 * 角色数据访问类
 *
 */
@Repository("roleDao")
public class RoleDao extends BaseDao<Role> implements IRoleDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Role role1,Role role2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Role.class);
        if(role1!=null){
            if(!StringUtils.isEmpty(role1.getName())){
                dc.add(Restrictions.like("name", role1.getName(), MatchMode.ANYWHERE));
            }

        }
        return dc;
    }
}
