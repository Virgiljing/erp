package cn.itcast.erp.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IGoodstypeDao;
import cn.itcast.erp.entity.Goodstype;

/**
 * 商品分类数据访问类
 *
 */
@Repository("goodstypeDao")
public class GoodstypeDao extends BaseDao<Goodstype> implements IGoodstypeDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Goodstype goodstype1,Goodstype goodstype2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Goodstype.class);
        if(goodstype1!=null){
            if(!StringUtils.isEmpty(goodstype1.getName())){
                dc.add(Restrictions.like("name", goodstype1.getName(), MatchMode.ANYWHERE));
            }

        }
        if(goodstype2!=null){
        	if(!StringUtils.isEmpty(goodstype2.getName())){
        		dc.add(Restrictions.eq("name", goodstype2.getName()));
        	}
        }
        return dc;
    }
}
