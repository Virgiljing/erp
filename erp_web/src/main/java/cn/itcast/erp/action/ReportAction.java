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

import cn.itcast.erp.biz.IReportBiz;
import cn.itcast.erp.util.WebUtil;

@Controller("reportAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("report")
public class ReportAction {

    private Date startDate;
    private Date endDate;
    private int year;

    @Autowired
    private IReportBiz reportBiz;

    /**
     * 销售统计报表
     */
    public void orderReport() {
        List<Map<String,Object>> list = reportBiz.orderReport(startDate, endDate);
        WebUtil.write(list);
    }

    /**
     * 销售趋势
     */
    public void trendReport() {
        List<Map<String, Object>> list = reportBiz.trendReport(year);
        WebUtil.write(list);
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
