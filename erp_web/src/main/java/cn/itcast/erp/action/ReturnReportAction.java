package cn.itcast.erp.action;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IReturnReportBiz;
import cn.itcast.erp.util.WebUtil;

@Controller("returnReportAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("returnReport")
public class ReturnReportAction {
	
	private int year;
	
	public void setYear(int year) {
		this.year = year;
	}

	@Autowired
	private IReturnReportBiz returnReportBiz;

	/**
     * 销售趋势
     */
    public void returnTrendReport() {
        List<Map<String, Object>> list = returnReportBiz.returnTrendReport(year);
        WebUtil.write(list);
    }
}
