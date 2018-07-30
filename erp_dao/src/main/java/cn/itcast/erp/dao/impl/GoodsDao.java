package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.entity.Goods;

/**
 * 商品数据访问类
 *
 */
@Repository("goodsDao")
public class GoodsDao extends BaseDao<Goods> implements IGoodsDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Goods goods1,Goods goods2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Goods.class);
        if(goods1!=null){
            if(!StringUtils.isEmpty(goods1.getName())){
                dc.add(Restrictions.like("name", goods1.getName(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(goods1.getOrigin())){
                dc.add(Restrictions.like("origin", goods1.getOrigin(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(goods1.getProducer())){
                dc.add(Restrictions.like("producer", goods1.getProducer(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(goods1.getUnit())){
                dc.add(Restrictions.like("unit", goods1.getUnit(), MatchMode.ANYWHERE));
            }
            // 商品类型
            if(null != goods1.getGoodstype() && null != goods1.getGoodstype().getUuid()) {
                dc.add(Restrictions.eq("goodstype", goods1.getGoodstype()));
            }

        }
        
        if (null != goods2) {
        	 if(!StringUtils.isEmpty(goods2.getName())){
        		 //根据商品名字获得商品
                 dc.add(Restrictions.eq("name", goods2.getName()));
             }
		}
        return dc;
    }
}
