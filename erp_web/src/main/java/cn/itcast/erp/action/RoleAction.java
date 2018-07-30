package cn.itcast.erp.action;

import java.util.List;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.util.WebUtil;

@Controller("roleAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("role")
public class RoleAction extends BaseAction<Role> {

    private IRoleBiz roleBiz;
    private String ids;// 菜单编号，多个以逗号分割

    @Resource(name="roleBiz")
    public void setRoleBiz(IRoleBiz roleBiz) {
        this.roleBiz = roleBiz;
        super.setBaseBiz(this.roleBiz);
    }

    /**
     * 读取角色下的菜单
     */
    public void readRoleMenus() {
        List<Tree> list = roleBiz.readRoleMenus(getId());
        WebUtil.write(list);
    }

    /**
     * 设置角色权限
     */
    public void updateRoleMenus() {
        try {
            roleBiz.updateRoleMenus(getId(),ids);
            WebUtil.ajaxReturn(true, "更新成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "更新失败");
        }
    }

    public void setIds(String ids) {
        this.ids = ids;
    }
}
