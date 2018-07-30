package cn.itcast.erp.biz;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 报表业务
 *
 */
public interface ISellReturnBiz {

	List<Map<String,Object>> SellReturnSum(Date startDate, Date endDate);
	
	List<Map<String,Object>> orderReport2(Long goodstypeuuid,Date startDate, Date endDate);
}
