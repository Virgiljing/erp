package cn.itcast.erp.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.ISellReturnBiz;
import cn.itcast.erp.util.WebUtil;

@Controller("sellReturnAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("sell")
public class SellReturnAction  {
	@Autowired
    private ISellReturnBiz sellReturnBiz;
    private Date  startDate;
    private Date endDate;
    private Long goodstypeuuid;
    
    
    
	public void setGoodstypeuuid(Long goodstypeuuid) {
		this.goodstypeuuid = goodstypeuuid;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	public void  sellReturnSum() {
    		List<Map<String, Object>> list = sellReturnBiz.SellReturnSum(startDate, endDate);
    		WebUtil.write(list);
	}
    
	public void orderReport2() {
			List<Map<String, Object>> list = sellReturnBiz.orderReport2(goodstypeuuid, startDate, endDate);
			WebUtil.write(list);
	}
    
}
