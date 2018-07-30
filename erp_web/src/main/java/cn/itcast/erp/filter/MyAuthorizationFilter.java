package cn.itcast.erp.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

public class MyAuthorizationFilter extends PermissionsAuthorizationFilter {

    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws IOException {

        Subject subject = getSubject(request, response);
        String[] perms = (String[]) mappedValue;

        // 没有标定权限，放行
        if(null == perms || perms.length == 0) {
            return true;
        }
        // 有标定权限, 遍历权限，判断用户是否拥有权限
        for (String perm : perms) {
            if(subject.isPermitted(perm)) {
                // 如果有一个权限符合，就放行
                return true;
            }
        }
        // 遍历完后，一个也没有符合,就要阻止
        return false;

    }
}
