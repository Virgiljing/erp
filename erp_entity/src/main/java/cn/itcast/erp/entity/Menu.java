package cn.itcast.erp.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 菜单实体类
 */
@Entity
@Table(name="menu")
public class Menu {

    public static final String MENUS_KEY = "team01_menuList_";

    @Id
    private String menuid;//菜单ID
    private String menuname;//菜单名称
    private String icon;//图标
    private String url;//URL
    //private String pid;//上级菜单ID

    @OneToMany(targetEntity=Menu.class)
    @JoinColumn(name="pid")
    @OrderBy("menuid asc")
    private List<Menu> menus;

    @ManyToMany(mappedBy="menus")
    @JSONField(serialize=false)
    private List<Role> roles; // 拥有这个权限的所有角色

    public List<Menu> getMenus() {
        return menus;
    }
    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
    public String getMenuid() {
        return menuid;
    }
    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }
    public String getMenuname() {
        return menuname;
    }
    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
