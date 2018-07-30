package cn.itcast.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IReportDao;

@Repository("reportDao")
@SuppressWarnings("unchecked")
public class ReportDao extends HibernateDaoSupport implements IReportDao {

    @Resource(name="sessionFactory")
    public void setSf(SessionFactory sf) {
        super.setSessionFactory(sf);
    }


    @Override
    public List<Map<String,Object>> orderReport(Date startDate, Date endDate) {
        String hql = "select new Map(gt.name as name,sum(od.money) as y) from Goodstype gt,Goods g,Orderdetail od, Orders o " +
                "where gt=g.goodstype and g.uuid=od.goodsuuid " +
                "and o=od.orders and o.type='2' ";
        List<Date> params = new ArrayList<Date>();
        if(null != startDate) {
            hql += "and o.createtime>=? ";
            params.add(startDate);
        }
        if(null != endDate) {
            hql += "and o.createtime<=? ";
            params.add(endDate);
        }
        hql+="group by gt.name";
        return (List<Map<String,Object>>)this.getHibernateTemplate().find(hql, params.toArray());
    }


    @Override
    public Map<String, Object> trendReport(int month, int year) {
        //select extract (month from sysdate) from dual;
        String hql="select new Map(month(o.createtime) as name,sum(od.money) as y) from Orders o, Orderdetail od " +
                "where o=od.orders and o.type='2' " +
                "and year(o.createtime)=? and month(o.createtime)=? " +
                "group by month(o.createtime)";
        List<?> list = this.getHibernateTemplate().find(hql, year,month);
        if(null != list && list.size() > 0) {
            return (Map<String, Object>)list.get(0);
        }
        return null;
    }

}
