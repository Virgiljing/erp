package cn.itcast.erp.biz;
import java.util.List;

import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
/**
 * 角色业务逻辑层接口
 *
 */
public interface IRoleBiz extends IBaseBiz<Role>{

    /**
     * 获取角色权限信息
     * @param uuid
     * @return
     */
    List<Tree> readRoleMenus(Long uuid);

    /**
     * 设置角色权限
     * @param id
     * @param ids
     */
    void updateRoleMenus(Long id, String ids);
}

