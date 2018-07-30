package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IRoleBiz;
import cn.itcast.erp.dao.IMenuDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 角色业务逻辑类
 *
 */
@Service("roleBiz")
public class RoleBiz extends BaseBiz<Role> implements IRoleBiz {

    private IRoleDao roleDao;
    @Autowired
    private IMenuDao menuDao;

    @Autowired
    private JedisPool jedisPool;

    @Resource(name="roleDao")
    public void setRoleDao(IRoleDao roleDao) {
        this.roleDao = roleDao;
        super.setBaseDao(this.roleDao);
    }

    @Override
    public List<Tree> readRoleMenus(Long uuid) {
        List<Tree> result = new ArrayList<Tree>();
        // 获取根菜单是因为它有上下级关系, getList没有上下级
        // 持久化
        Menu root = menuDao.get("0");

        // 获取角色信息 持久化
        Role role = roleDao.get(uuid);
        // 得到 角色所拥有的权限
        List<Menu> roleMenus = role.getMenus();

        // 所有的一级菜单
        List<Menu> l1Menus = root.getMenus();
        // l1Menu进入持久态
        for (Menu l1Menu : l1Menus) {
            // 一级菜单树节点
            Tree l1 = createTree(l1Menu);
            // 获取二级菜单
            List<Menu> l2Menus = l1Menu.getMenus();
            for (Menu l2Menu : l2Menus) {
                Tree l2 = createTree(l2Menu);
                // 判断角色是否拥有这个角色
                if(roleMenus.contains(l2Menu)){
                    // 让它选中
                    l2.setChecked(true);
                }

                // 一级菜单下的子菜单, 一级菜单节点下
                l1.getChildren().add(l2);
            }
            result.add(l1);
        }
        return result;
    }

    /**
     * 根据菜单创建树的节点
     * @param menu
     * @return
     */
    private Tree createTree(Menu menu) {
        Tree tree = new Tree();
        tree.setId(menu.getMenuid());
        tree.setText(menu.getMenuname());
        tree.setChildren(new ArrayList<Tree>());
        return tree;
    }

    @Override
    @Transactional
    public void updateRoleMenus(Long id, String ids) {
        // 获取角色，进入持久态
        Role role = roleDao.get(id);
        // 清空角色下的权限，删除关系
        // delete from role_menu where roleuuid=?
        role.setMenus(new ArrayList<Menu>());


        // 创建新的关系
        String[] menuIds = ids.split(",");
        for (String menuid : menuIds) {
            // menu进入持久态
            Menu menu = menuDao.get(menuid);
            // 建立关系
            role.getMenus().add(menu);
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 拥有这个角色的所有用户, 清空每个用户的缓存权限
            List<Emp> emps = role.getEmps();
            for (Emp emp : emps) {
                jedis.del(Menu.MENUS_KEY + emp.getUuid());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != jedis) {
                jedis.close();
            }
            jedis = null;
        }


    }

}
