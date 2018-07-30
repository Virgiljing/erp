package cn.itcast.erp.action;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("orderdetailAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("orderdetail")
public class OrderdetailAction extends BaseAction<Orderdetail> {

    private IOrderdetailBiz orderdetailBiz;
    private Long storeuuid;//仓库编号

    public void setStoreuuid(Long storeuuid) {
        this.storeuuid = storeuuid;
    }

    @Resource(name="orderdetailBiz")
    public void setOrderdetailBiz(IOrderdetailBiz orderdetailBiz) {
        this.orderdetailBiz = orderdetailBiz;
        super.setBaseBiz(this.orderdetailBiz);
    }

    /**
     * 入库
     */
    public void doInStore() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        try {
            orderdetailBiz.doInStore(loginUser.getUuid(), storeuuid, getId());
            WebUtil.ajaxReturn(true, "入库成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "入库失败");
        }
    }

    /**
     * 出库
     */
    public void doOutStore() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        try {
            orderdetailBiz.doOutStore(loginUser.getUuid(), storeuuid, getId());
            WebUtil.ajaxReturn(true, "出库成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "出库失败");
        }
    }

    /**
     * 出库
     */
    public void doOutStoreSPC() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        try {
            Map<String, Object> result = orderdetailBiz.doOutStoreSPC(loginUser.getUuid(), storeuuid, getId());
            WebUtil.write(result);
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "出库失败");
        }
    }
}
