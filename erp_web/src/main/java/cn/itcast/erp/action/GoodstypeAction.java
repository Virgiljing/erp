package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IGoodstypeBiz;
import cn.itcast.erp.entity.Goodstype;

@Controller("goodstypeAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("goodstype")
public class GoodstypeAction extends BaseAction<Goodstype> {

    private IGoodstypeBiz goodstypeBiz;

    @Resource(name="goodstypeBiz")
    public void setGoodstypeBiz(IGoodstypeBiz goodstypeBiz) {
        this.goodstypeBiz = goodstypeBiz;
        super.setBaseBiz(this.goodstypeBiz);
    }
}
