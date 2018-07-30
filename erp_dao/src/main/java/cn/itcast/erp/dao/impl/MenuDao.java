package cn.itcast.erp.dao.impl;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.entity.Menu;

/**
 * 菜单数据访问类
 *
 */
@Repository("menuDao")
@SuppressWarnings("unchecked")
public class MenuDao extends BaseDao<Menu> implements IMenuDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Menu menu1,Menu menu2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Menu.class);

        return dc;
    }

    @Override
    public List<Menu> getMenusByEmpuuid(Long empuuid) {
        // getEmp emp.getroles, 5表查询
        String hql = "select m from Emp e join e.roles r join r.menus m where e.uuid=?";
        return (List<Menu>) this.getHibernateTemplate().find(hql, empuuid);
    }
}
