 package cn.itcast.erp.biz;

import java.util.List;
import java.util.Map;

public interface IReturnReportBiz {
	
	/**
     * 销售退货趋势
     * @param month
     * @param year
     * @return
     */
    List<Map<String, Object>> returnTrendReport(int year);
}
