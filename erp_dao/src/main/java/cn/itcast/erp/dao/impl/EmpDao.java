package cn.itcast.erp.dao.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.entity.Emp;

/**
 * 员工数据访问类
 *
 */
@Repository("empDao")
public class EmpDao extends BaseDao<Emp> implements IEmpDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Emp emp1,Emp emp2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Emp.class);
        if(emp1!=null){
            if(!StringUtils.isEmpty(emp1.getUsername())){
                dc.add(Restrictions.like("username", emp1.getUsername(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(emp1.getName())){
                dc.add(Restrictions.like("name", emp1.getName(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(emp1.getEmail())){
                dc.add(Restrictions.like("email", emp1.getEmail(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(emp1.getTele())){
                dc.add(Restrictions.like("tele", emp1.getTele(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(emp1.getAddress())){
                dc.add(Restrictions.like("address", emp1.getAddress(), MatchMode.ANYWHERE));
            }
            // 性别
            if(null != emp1.getGender()){
                dc.add(Restrictions.eq("gender", emp1.getGender()));
            }
            // 部门
            if(null != emp1.getDep() && null != emp1.getDep().getUuid()){
                dc.add(Restrictions.eq("dep", emp1.getDep()));
            }
            // 开始日期 >=
            if(null != emp1.getBirthday()) {
                dc.add(Restrictions.ge("birthday", emp1.getBirthday()));
            }
        }
        if(null != emp2) {
            // 结束日期 <=
            if(null != emp2.getBirthday()) {
                dc.add(Restrictions.le("birthday", emp2.getBirthday()));
            }
        }

        return dc;
    }

    @Override
    public Emp findByUsernameAndPwd(String username, String pwd) {
        String hql = "from Emp where username=? and pwd=?";
        List<?> list = this.getHibernateTemplate().find(hql, username,pwd);
        if(null != list && list.size() > 0) {
            // 如果能查询到用户信息就返回第一个
            return (Emp) list.get(0);
        }
        return null;
    }

    @Override
    public void updatePwd(String newPwd, Long uuid) {
        String hql = "update Emp set pwd=? where uuid=?";
        // 通过语句来执行update/delete操作
        this.getHibernateTemplate().bulkUpdate(hql, newPwd, uuid);
    }
}
