package cn.itcast.erp.dao.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IReturnReportDao;

@Repository("returnReportDao")
@SuppressWarnings("unchecked")
public class ReturnReportDao extends HibernateDaoSupport implements IReturnReportDao {
	
	@Resource(name="sessionFactory")
	public void setSf(SessionFactory sf) {
        super.setSessionFactory(sf);
    }
	
	@Override
	public Map<String, Object> returnTrendReport(int month, int year) {
		String hql="select new Map(month(ro.createtime) as name,sum(rod.money) as y) from Returnorders ro, Returnorderdetail rod " +
                "where ro=rod.returnorders and ro.type='2' " +
                "and year(ro.createtime)=? and month(ro.createtime)=? " +
                "group by month(ro.createtime)";
		/*select new Map(month(o.createtime) as name,sum(od.money) as y) from Orders o, Orderdetail od " +
        "where o=od.orders and o.type='2' " +
        "and year(o.createtime)=? and month(o.createtime)=? " +
        "group by month(o.createtime)*/
        List<?> list = this.getHibernateTemplate().find(hql, year,month);
        if(null != list && list.size() > 0) {
            return (Map<String, Object>)list.get(0);
        }
        return null;
	}

}
