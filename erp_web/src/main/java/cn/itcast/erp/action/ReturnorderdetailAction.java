package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IReturnorderdetailBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Returnorderdetail;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("returnorderdetailAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("returnorderdetail")
public class ReturnorderdetailAction extends BaseAction<Returnorderdetail> {

    private IReturnorderdetailBiz returnorderdetailBiz;
    
    private Long storeuuid; //仓库编号
    
    public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}

	@Resource(name="returnorderdetailBiz")
    public void setReturnorderdetailBiz(IReturnorderdetailBiz returnorderdetailBiz) {
        this.returnorderdetailBiz = returnorderdetailBiz;
        super.setBaseBiz(this.returnorderdetailBiz);
    }
	
	    /**
	     * 销售退货入库
	     */
	    public void doInStore() {
	        // 判断用户是否登陆
	        Emp loginUser = WebUtil.getLoginUser();
	        if(null == loginUser) {
	            WebUtil.ajaxReturn(false, "你还没有登陆");
	            return;
	        }
	        try {
	            returnorderdetailBiz.doInStore(loginUser.getUuid(),storeuuid, getId());
	            WebUtil.ajaxReturn(true, "销售退货成功");
	        } catch (ErpException e) {
	            e.printStackTrace();
	            WebUtil.ajaxReturn(false, e.getMessage());
	        } catch (Exception e) {
	            e.printStackTrace();
	            WebUtil.ajaxReturn(false, "销售退货失败");
	        }
	    }
    /**
     * 采购退货出库
     */
    public void returnOrderOutStore(){
    	//判断当前用户是否登录
    	Emp loginUser = WebUtil.getLoginUser();
    	if(null == loginUser){
    		WebUtil.ajaxReturn(false, "请先登录");
    	}
    	try {
			returnorderdetailBiz.returnOrderOutStore(loginUser.getUuid(), storeuuid, getId());
			WebUtil.ajaxReturn(true, "退货出库成功");
    	} catch (ErpException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "退货出库失败");
		}
    }
}
