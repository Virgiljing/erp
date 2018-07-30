package cn.itcast.erp.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.redsun.bos.ws.Waybilldetail;
import com.redsun.bos.ws.impl.IWaybillWs;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("ordersAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("orders")
public class OrdersAction extends BaseAction<Orders> {

    private static final Logger log = LoggerFactory.getLogger(OrdersAction.class);

    private IOrdersBiz ordersBiz;
    @Autowired
    private IWaybillWs waybillWs;
    private String json;// 所有明细的json字符串
    private Long waybillsn; // 运单号

    public void setJson(String json) {
        this.json = json;
    }

    @Resource(name="ordersBiz")
    public void setOrdersBiz(IOrdersBiz ordersBiz) {
        this.ordersBiz = ordersBiz;
        super.setBaseBiz(this.ordersBiz);
    }

    /**
     * 采购申请
     */
    @Override
    public void add() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        //System.out.println(getT());
        try {
            // 获取前端提交的订单,主要是得到供应商的编号
            Orders orders = getT();
            // 把明细的json字符串转成订单明细列表
            List<Orderdetail> orderDetails = JSON.parseArray(json, Orderdetail.class);
            // 设置订单下的明细
            orders.setOrderDetails(orderDetails);
            // 设置下单人
            orders.setCreater(loginUser.getUuid());
            // 设置订单的类型
            orders.setType(Orders.TYPE_IN);
            orders.setState(Orders.STATE_CREATE);
            ordersBiz.add(orders);
            WebUtil.ajaxReturn(true, "添加订单成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "添加订单失败");
        }
    }

    /**
     * 订单审核
     */
    public void doCheck() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        try {
            log.debug("订单编号:" + (null==getId()?"null":getId()));
            ordersBiz.doCheck(loginUser.getUuid(), getId());
            WebUtil.ajaxReturn(true, "审核成功");
        } catch (ErpException e) {
            log.error("业务异常",e);
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            log.error("未知异常",e);
            WebUtil.ajaxReturn(false, "审核失败");
        }
    }

    /**
     * 订单确认
     */
    public void doStart() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        try {
            log.debug("订单编号:" + (null==getId()?"null":getId()));
            ordersBiz.doStart(loginUser.getUuid(), getId());
            WebUtil.ajaxReturn(true, "确认成功");
        } catch (ErpException e) {
            log.error("业务异常",e);
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            log.error("未知异常",e);
            WebUtil.ajaxReturn(false, "确认失败");
        }
    }

    /**
     * 查询由我登陆用户发起订单
     */
    public void myListByPage() {
        Emp loginUser = WebUtil.getLoginUser();
        if(null != loginUser) {
            // 判断查询条件
            if(null == getT1()) {
                // 构建查询条件
                setT1(new Orders());
            }
            // 设置查询条件为登陆用户,根据下单员查询
            getT1().setCreater(loginUser.getUuid());
            super.listByPage();
        }
    }

    /**
     * 销售订单录入
     */
    public void add_out() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        //System.out.println(getT());
        try {
            // 获取前端提交的订单,主要是得到供应商的编号
            Orders orders = getT();
            // 把明细的json字符串转成订单明细列表
            List<Orderdetail> orderDetails = JSON.parseArray(json, Orderdetail.class);
            // 设置订单下的明细
            orders.setOrderDetails(orderDetails);
            // 设置下单人
            orders.setCreater(loginUser.getUuid());
            // 设置订单类型为销售
            orders.setType(Orders.TYPE_OUT);
            // 订单的状态为未出库
            orders.setState(Orders.STATE_NOT_OUT);
            ordersBiz.add(orders);
            WebUtil.ajaxReturn(true, "添加订单成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "添加订单失败");
        }
    }


    /**
     * 导出订单
     */
    public void exportById() {
        HttpServletResponse res = ServletActionContext.getResponse();
        String filename = String.format("orders_%d.xls",getId());

        try {
            // 告诉浏览器，下载文件
            res.setHeader("Content-Disposition", "attachment;filename=" + filename);
            ordersBiz.exportById(res.getOutputStream(), getId());
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 查询物流路径信息
     */
    public void waybilldetailList() {
        List<Waybilldetail> list = waybillWs.getWaybilldetailList(waybillsn);
        WebUtil.write(list);
    }

    public void setWaybillsn(Long waybillsn) {
        this.waybillsn = waybillsn;
    }

    /*
    public static void main(String[] args) {
        String filename = String.format("orders_%03d.xls",11);
        System.out.println(filename);
    }*/






}
