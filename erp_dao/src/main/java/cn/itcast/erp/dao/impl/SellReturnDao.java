package cn.itcast.erp.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.ISellReturnDao;

@Repository("sellReturnDao")
@SuppressWarnings("unchecked")
public class SellReturnDao extends HibernateDaoSupport implements ISellReturnDao {

    @Resource(name="sessionFactory")
    public void setSf(SessionFactory sf) {
        super.setSessionFactory(sf);
    }

    @Override
    public List<Map<String,Object>> SellReturnSum(Date startDate, Date endDate) {
        String hql = "select new Map( gt.name as name , sum(rod.money) as y,gt.uuid as goodstypeuuid ) from Goodstype gt ,Goods g,Returnorderdetail rod, Returnorders ro  " + 
        		"where gt= g.goodstype and g.uuid=rod.goodsuuid  " + 
        		"and ro = rod.returnorders and  ro.type=2  " ;
        List<Date> params = new ArrayList<Date>();
        if(null != startDate) {
            hql += "and ro.createtime>=? ";
            params.add(startDate);
        }
        if(null != endDate) {
            hql += "and ro.createtime<=? ";
            params.add(endDate);
        }
        hql+="group by gt.name, gt.uuid";
        return (List<Map<String,Object>>)this.getHibernateTemplate().find(hql, params.toArray());
    }

    @Override
    public List<Map<String,Object>> orderReport2(Long goodstypeuuid,Date startDate, Date endDate) {
        String hql = "select new Map(g.uuid as uuid,g.name as name,sum(rod.money) as y) "
                + "from Goods g,Returnorderdetail rod,Returnorders ro " +
                "where g.uuid=rod.goodsuuid and ro = rod.returnorders and ro.type='2' and g.goodstype.uuid=? ";
        List<Object> params = new ArrayList<Object>();
        params.add(goodstypeuuid);
        if(null != startDate) {
            hql += "and ro.createtime>=? ";
            params.add(startDate);
        }
        if(null != endDate) {
            hql += "and ro.createtime<=? ";
            params.add(endDate);
        }
        hql+="group by g.uuid,g.name";
        return (List<Map<String,Object>>)this.getHibernateTemplate().find(hql, params.toArray());
    }
	

}
