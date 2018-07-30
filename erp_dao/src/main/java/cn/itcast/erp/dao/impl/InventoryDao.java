package cn.itcast.erp.dao.impl;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.itcast.erp.dao.IInventoryDao;
import cn.itcast.erp.entity.Inventory;

/**
 * 盘盈盘亏数据访问类
 *
 */
@Repository("inventoryDao")
public class InventoryDao extends BaseDao<Inventory> implements IInventoryDao {

    /**
     * 构建查询条件
     * @param t1
     * @param t2
     * @param param
     * @return
     */
    @Override
    public DetachedCriteria getDetachedCriteria(Inventory inventory1,Inventory inventory2,Object param){
        DetachedCriteria dc=DetachedCriteria.forClass(Inventory.class);
        if(inventory1!=null){
            if(!StringUtils.isEmpty(inventory1.getType())){
                dc.add(Restrictions.like("type", inventory1.getType(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(inventory1.getState())){
                dc.add(Restrictions.like("state", inventory1.getState(), MatchMode.ANYWHERE));
            }
            if(!StringUtils.isEmpty(inventory1.getRemark())){
                dc.add(Restrictions.like("remark", inventory1.getRemark(), MatchMode.ANYWHERE));
            }
            //查询登记时间[开始]
            if (null != inventory1.getCreatetime()) {
				dc.add(Restrictions.ge("createtime", inventory1.getCreatetime()));
			}
          //查询审核时间[开始]
            if (null != inventory1.getChecktime()) {
				dc.add(Restrictions.ge("checktime", inventory1.getChecktime()));
			}
        }
        
        if (null != inventory2) {
        	//查询登记时间[截止]
            if (null != inventory2.getCreatetime()) {
            	Date endDay = endDay(inventory2.getCreatetime());
				dc.add(Restrictions.le("createtime", endDay));
			}
            //查询审核时间[截止]
            if (null != inventory2.getChecktime()) {
            	Date endDay = endDay(inventory2.getChecktime());
				dc.add(Restrictions.le("checktime", endDay));
			}
		}
        return dc;
    }
    
    /**
     * 私有方法,把传入的date 00:00:00改成23:59:59返回
     */
    private Date endDay(Date date) {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.set(Calendar.HOUR_OF_DAY, 23);
    	calendar.set(Calendar.MINUTE, 59);
    	calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}
}

