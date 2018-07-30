package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Menu;
/**
 * 菜单业务逻辑层接口
 *
 */
public interface IMenuBiz extends IBaseBiz<Menu>{

    /**
     * 获取登陆用户所拥有权限菜单
     * @param empuuid
     * @return
     */
    List<Menu> getMenusByEmpuuid(Long empuuid);

    /**
     * 获取登陆用户的菜单
     * @param empuuid
     * @return
     */
    Menu readMenusByEmpuuid(Long empuuid);
}

