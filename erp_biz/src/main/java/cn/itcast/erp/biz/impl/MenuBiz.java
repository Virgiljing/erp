package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.entity.Menu;

/**
 * 菜单业务逻辑类
 *
 */
@Service("menuBiz")
public class MenuBiz extends BaseBiz<Menu> implements IMenuBiz {

    private IMenuDao menuDao;

    @Resource(name="menuDao")
    public void setMenuDao(IMenuDao menuDao) {
        this.menuDao = menuDao;
        super.setBaseDao(this.menuDao);
    }

    @Override
    public List<Menu> getMenusByEmpuuid(Long empuuid) {
        return menuDao.getMenusByEmpuuid(empuuid);
    }

    @Override
    public Menu readMenusByEmpuuid(Long empuuid) {
        // 获取菜单模板
        Menu root = menuDao.get("0");
        // 获取用户的权限菜单集合
        List<Menu> empMenus = getMenusByEmpuuid(empuuid);
        // 复制根菜单{}
        Menu _root = cloneMenu(root);
        // 遍历模板进行复制, 模板中的一级菜单
        for (Menu l1menu : root.getMenus()) {
            // 复制一级菜单
            Menu _l1 = cloneMenu(l1menu);
            // 遍历二级菜单
            for (Menu l2menu : l1menu.getMenus()) {
                // 用户的菜单集合中是否包含这个菜单
                if(empMenus.contains(l2menu)) {
                    // 复制二级菜单
                    Menu _l2 = cloneMenu(l2menu);
                    // 添加到要复制的一级菜单中
                    _l1.getMenus().add(_l2);
                }
            }

            if(_l1.getMenus().size() > 0) {
                // 复制后的一级菜单下有二级菜单，就要加进来
                _root.getMenus().add(_l1);
            }
        }
        return _root;
    }

    private Menu cloneMenu(Menu src) {
        Menu menu = new Menu();
        menu.setIcon(src.getIcon());
        menu.setMenuid(src.getMenuid());
        menu.setMenuname(src.getMenuname());
        menu.setUrl(src.getUrl());
        menu.setMenus(new ArrayList<Menu>());
        return menu;
    }
}
