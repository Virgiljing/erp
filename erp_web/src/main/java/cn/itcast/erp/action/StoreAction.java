package cn.itcast.erp.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IStoreBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Store;
import cn.itcast.erp.util.WebUtil;

@Controller("storeAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("store")
public class StoreAction extends BaseAction<Store> {

    private IStoreBiz storeBiz;

    @Resource(name="storeBiz")
    public void setStoreBiz(IStoreBiz storeBiz) {
        this.storeBiz = storeBiz;
        super.setBaseBiz(this.storeBiz);
    }

    public void myList() {
        Emp loginUser = WebUtil.getLoginUser();
        if(loginUser != null) {
            // 构建查询条件
            Store store = new Store();
            // 设置查询条件的库管员
            store.setEmpuuid(loginUser.getUuid());
            List<Store> list = storeBiz.getList(store, null, null);
            WebUtil.write(list);
        }
        // 不返回任何内容
    }
}
