package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IDepBiz;
import cn.itcast.erp.entity.Dep;

@Controller("depAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("dep")
public class DepAction extends BaseAction<Dep> {

    private IDepBiz depBiz;

    @Resource(name="depBiz")
    public void setDepBiz(IDepBiz depBiz) {
        this.depBiz = depBiz;
        super.setBaseBiz(this.depBiz);
    }
}
