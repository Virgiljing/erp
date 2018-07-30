package cn.itcast.erp.biz.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.ISellReturnBiz;
import cn.itcast.erp.dao.ISellReturnDao;

@Service("sellReturnBiz")
public class SellReturnBiz implements ISellReturnBiz {

    @Autowired
    private ISellReturnDao sellReturnDao;

	@Override
	public List<Map<String, Object>> SellReturnSum(Date startDate, Date endDate) {
		List<Map<String,Object>> sellReturnSum = sellReturnDao.SellReturnSum(startDate, endDate);
		
		return sellReturnSum;
	}

	@Override
	public List<Map<String, Object>> orderReport2(Long goodstypeuuid, Date startDate, Date endDate) {
		
		return  sellReturnDao.orderReport2(goodstypeuuid, startDate, endDate);
	}

}
