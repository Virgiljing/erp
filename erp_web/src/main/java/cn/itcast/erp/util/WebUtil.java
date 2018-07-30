package cn.itcast.erp.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.struts2.ServletActionContext;

import com.alibaba.fastjson.JSON;

import cn.itcast.erp.entity.Emp;

public class WebUtil {

    /**
     * 把json格式的字符串输出给前端
     * @param jsonString
     */
    public static void write(String jsonString) {
        try {
            HttpServletResponse res = ServletActionContext.getResponse();
            // 告诉浏览器使用utf-8来解析输出的内容
            res.setContentType("text/html;charset=utf-8");
            PrintWriter writer = res.getWriter();
            // 输出json格式的字符串给前端
            writer.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 把对象转成json字符串输出给前端
     * @param obj
     */
    public static void write(Object obj) {
        String jsonString = JSON.toJSONString(obj);
        WebUtil.write(jsonString);
    }

    /**
     * 处理操作结果返回给前端
     * @param success
     * @param message
     */
    public static void ajaxReturn(boolean success,String message) {
        Map<String,Object> rtn = new HashMap<String,Object>();
        rtn.put("success", success);
        rtn.put("message", message);
        WebUtil.write(rtn);
    }

    /**
     * 获取登陆用户
     * @return
     */
    public static Emp getLoginUser() {

        return (Emp)SecurityUtils.getSubject().getPrincipal();
    }
}
