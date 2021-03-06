package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IReturnReportBiz;
import cn.itcast.erp.dao.IReturnReportDao;

@Service("returnReportBiz")
public class ReturnReportBiz implements IReturnReportBiz {
	
	@Autowired
	private IReturnReportDao returnReportDao;
	
	@Override
	public List<Map<String, Object>> returnTrendReport(int year) {
		// 保存12个月的数据
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        // 循环12个月，分别每个月查询数据
        Map<String, Object> monthData = null;
        for(int i = 1; i <= 12; i++) {
            monthData = returnReportDao.returnTrendReport(i, year);
            if(null == monthData) {
                // 没有这个月的销售额，要补0
                //{name:1,y:0}
                monthData = new HashMap<String,Object>();
                monthData.put("name", i);
                monthData.put("y", 0);
            }
            result.add(monthData);
        }
        return result;
	}

}
