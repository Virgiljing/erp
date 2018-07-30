package cn.itcast.erp.realm;

import java.util.List;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.biz.IMenuBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 自定realm
 * 实现认证与授权
 *
 */
public class ErpRealm extends AuthorizingRealm {

    @Autowired
    private IEmpBiz empBiz;

    @Autowired
    private IMenuBiz menuBiz;

    @Autowired
    private JedisPool jedisPool;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("获取授权信息... 对登陆进行授权");
        // 得到登陆用户信息
        Emp loginUser = (Emp)principals.getPrimaryPrincipal();
        Jedis jedis = null;
        String menusString = null;
        List<Menu> empMenus = null;
        try {
            jedis = jedisPool.getResource();
            // 从redis缓存获取权限集合的字符串
            menusString = jedis.get(Menu.MENUS_KEY + loginUser.getUuid());
            if(null == menusString) {
                // 查询登陆用户的权限集合
                empMenus = menuBiz.getMenusByEmpuuid(loginUser.getUuid());
                // 所有的权限集合转成json字符串
                menusString = JSON.toJSONString(empMenus);
                // 放入redis的缓存中
                jedis.set(Menu.MENUS_KEY + loginUser.getUuid(), menusString);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(null != jedis) {
                jedis.close();
            }
            jedis = null;
        }
        // redis出错时
        if(null == menusString) {
            // 查询登陆用户的权限集合
            empMenus = menuBiz.getMenusByEmpuuid(loginUser.getUuid());
        }else {
            // 把字符串转成权限集合
            empMenus = JSON.parseArray(menusString, Menu.class);
        }
        // 存储用户的权限信息 钱包
        SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
        // 授权
        for (Menu menu : empMenus) {
            // 使用菜单名授权给登陆用户，因为标定url访问权限用的是菜单的名称
            sai.addStringPermission(menu.getMenuname());
        }
        //sai.addStringPermission("供应商");
        sai.addRole("管理员");
        return sai;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("获取认证信息... 校验用户是否存在");
        // 转成登陆令牌, loginAction，前端传过来
        UsernamePasswordToken upt = (UsernamePasswordToken)token;
        String username = upt.getUsername();
        String pwd = new String(upt.getPassword());
        // 查询用户信息
        Emp emp = empBiz.findByUsernameAndPwd(username, pwd);
        if(null != emp) {
            // 登陆验证通过, 认证信息
            //principal  当事人，  emp
            //credentials 凭证 pwd     认证器中有密码匹配器，取出token中的密码和Info中的密码进行比较
            //realmName realm对象标识 获得当事人信息
            SimpleAuthenticationInfo sai = new SimpleAuthenticationInfo(emp,pwd,getName());
            return sai;
        }
        return null;
    }

}
