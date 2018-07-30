package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IStoreoperBiz;
import cn.itcast.erp.entity.Storeoper;

@Controller("storeoperAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("storeoper")
public class StoreoperAction extends BaseAction<Storeoper> {

    private IStoreoperBiz storeoperBiz;

    @Resource(name="storeoperBiz")
    public void setStoreoperBiz(IStoreoperBiz storeoperBiz) {
        this.storeoperBiz = storeoperBiz;
        super.setBaseBiz(this.storeoperBiz);
    }
}
