package cn.itcast.erp.action;

import java.util.List;

import javax.annotation.Resource;
import javax.mail.MessagingException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IStoredetailBiz;
import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("storedetailAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("storedetail")
public class StoredetailAction extends BaseAction<Storedetail> {

    private IStoredetailBiz storedetailBiz;

    @Resource(name="storedetailBiz")
    public void setStoredetailBiz(IStoredetailBiz storedetailBiz) {
        this.storedetailBiz = storedetailBiz;
        super.setBaseBiz(this.storedetailBiz);
    }

    /**
     * 库存预警列表
     */
    public void storealertList() {
        List<Storealert> list = storedetailBiz.getStorealertList();
        WebUtil.write(list);
    }

    /**
     * 发送库存预警邮件
     */
    public void sendStorealertMail() {
        try {
            storedetailBiz.sendStorealertMail();
            WebUtil.ajaxReturn(true, "发送预警邮件成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (MessagingException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "发送预警邮件失败");
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "未知错误，请联系管理员");
        }
    }
}
