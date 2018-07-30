package cn.itcast.erp.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 角色数据访问接口
 *
 */
public interface ISellReturnDao {
	
	List<Map<String,Object>> SellReturnSum(Date startDate, Date endDate);
	
	
	
	List<Map<String,Object>> orderReport2(Long goodstypeuuid,Date startDate, Date endDate);
}
