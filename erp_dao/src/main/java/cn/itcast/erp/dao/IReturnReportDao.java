package cn.itcast.erp.dao;

import java.util.Map;

/**
 * 销售退货分析
 */
public interface IReturnReportDao {
	
	/**
     * 销售退货趋势
     * @param month
     * @param year
     * @return
     */
    Map<String,Object> returnTrendReport(int month,int year);
	
}
