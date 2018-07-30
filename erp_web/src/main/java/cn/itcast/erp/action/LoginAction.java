package cn.itcast.erp.action;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.util.WebUtil;

@Controller("loginAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("login")
public class LoginAction {

    @Autowired
    private IEmpBiz empBiz;

    private String username; // 登陆名
    private String pwd; // 密码

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 登陆校验
     */
    /*public void checkUser() {
        try {
            // 查询用户是否存在
            Emp loginUser = empBiz.findByUsernameAndPwd(username,pwd);
            if(null == loginUser) {
                // 查询不到用户信息
                WebUtil.ajaxReturn(false, "用户名或密码不正确!");
                return;
            }

            // 放到session
            ServletActionContext.getRequest().getSession().setAttribute("loginUser", loginUser);
            // 成功也要通知
            WebUtil.ajaxReturn(true, "登陆成功");
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "登陆失败");
        }
    }*/

    public void checkUser() {
        // 取出subject, 当事人，作用：封装登陆用户有关权限的一系列方法
        Subject subject = SecurityUtils.getSubject();
        // 令牌：身份证，火车票
        UsernamePasswordToken upt = new UsernamePasswordToken(username,pwd);
        // 用户登陆了
        try {
            subject.login(upt);
            WebUtil.ajaxReturn(true, "登陆成功");
        } catch (AuthenticationException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "用户名或密码错误");
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "登陆失败");
        }
    }

    /**
     * 显示登陆的用户名
     */
    public void showName() {
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "没有登陆");
        }else {
            WebUtil.ajaxReturn(true, loginUser.getName());
        }
    }

    /**
     * 退出登陆
     */
    public void loginOut() {
        SecurityUtils.getSubject().logout();
    }
}
