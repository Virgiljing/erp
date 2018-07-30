package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.util.WebUtil;

@Controller("menuAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("menu")
public class MenuAction extends BaseAction<Menu> {

    private IMenuBiz menuBiz;

    @Resource(name="menuBiz")
    public void setMenuBiz(IMenuBiz menuBiz) {
        this.menuBiz = menuBiz;
        super.setBaseBiz(this.menuBiz);
    }

    /**
     * 读取菜单信息
     */
    public void getMenuTree() {
        // 系统菜单, root对象进入持久化状态
        //Menu root = menuBiz.get("0");
        // fastJSON在转成json格式字符串时会自动完成以下动作
        /*// 触发对象导航 select * from menu where pid=0;
        List<Menu> menus = root.getMenus(); // 一级菜单 基础数据 人事管理
        // 循环一级菜单
        for (Menu l1menu : menus) {
            // l1menu 也是持久化状态, 触发对象导航
            List<Menu> menus2 = l1menu.getMenus();
            for(Menu l2menu : menus2) {
                 ...
            }
        }*/
        Emp loginUser = WebUtil.getLoginUser();
        if(null != loginUser) {
            //List<Menu> list = menuBiz.getMenusByEmpuuid(loginUser.getUuid());
            Menu root = menuBiz.readMenusByEmpuuid(loginUser.getUuid());
            WebUtil.write(root);
        }

    }
}
