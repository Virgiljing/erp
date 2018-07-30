package cn.itcast.erp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.util.WebUtil;

@Controller("supplierAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("supplier")
public class SupplierAction extends BaseAction<Supplier> {

    private ISupplierBiz supplierBiz;
    private String q;// combogrid中的mode设置为remote时，自动发送的参数
    private File file;// input name=file; 文件对象, 大小走过2M，值为null
    private String fileFileName; // 文件名
    private String fileContentType; // 文件的类型

    @Resource(name="supplierBiz")
    public void setSupplierBiz(ISupplierBiz supplierBiz) {
        this.supplierBiz = supplierBiz;
        super.setBaseBiz(this.supplierBiz);
    }

    public void setQ(String q) {
        this.q = q;
    }

    @Override
    public void list() {
        if(null == getT1()) {
            // 构建查询条件
            setT1(new Supplier());
        }
        getT1().setName(q);
        super.list();
    }

    /**
     * 导出数据
     */
    public void export() {
        HttpServletResponse res = ServletActionContext.getResponse();
        String filename = "供应商.xls";
        if(Supplier.TYPE_CUSTOMER.equals(getT1().getType())) {
            filename = "客户.xls";
        }
        try {
            // 文件名含有中文，转成原始的字节流，再以iso-8859-1编码形式组装字符串? 因为浏览读取响应头时用的iso-8859-1.
            filename = new String(filename.getBytes(),"iso-8859-1").toString();
            // 告诉浏览器，下载文件
            res.setHeader("Content-Disposition", "attachment;filename=" + filename);
            supplierBiz.export(res.getOutputStream(), getT1());
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 导入数据
     */
    public void doImport() {
        if(!"application/vnd.ms-excel".equals(fileContentType)) {
            if(!fileFileName.endsWith(".xls")) {
                WebUtil.ajaxReturn(false, "文件格式不正确!");
                return;
            }
        }
        try {
            supplierBiz.doImport(new FileInputStream(file));
            WebUtil.ajaxReturn(true, "导入成功");
        } catch (IOException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "导入失败");
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "导入失败，发生未知错误，请联系管理员");
        }
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setFileFileName(String fileFileName) {
        this.fileFileName = fileFileName;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }
}
