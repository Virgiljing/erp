package cn.itcast.erp.dao;

import java.util.List;

import cn.itcast.erp.entity.Menu;

/**
 * 菜单数据访问接口
 *
 */
public interface IMenuDao extends IBaseDao<Menu>{

    /**
     * 获取登陆用户所拥有权限菜单
     * @param empuuid
     * @return
     */
    List<Menu> getMenusByEmpuuid(Long empuuid);
}
