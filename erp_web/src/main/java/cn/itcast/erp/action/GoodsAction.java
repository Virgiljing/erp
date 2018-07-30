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

import cn.itcast.erp.biz.IGoodsBiz;
import cn.itcast.erp.entity.Goods;
import cn.itcast.erp.util.WebUtil;

@Controller("goodsAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("goods")
public class GoodsAction extends BaseAction<Goods> {

    private IGoodsBiz goodsBiz;

    @Resource(name="goodsBiz")
    public void setGoodsBiz(IGoodsBiz goodsBiz) {
        this.goodsBiz = goodsBiz;
        super.setBaseBiz(this.goodsBiz);
    }
    
    /**
     * 导出商品数据
     */
    public void export() {
        HttpServletResponse res = ServletActionContext.getResponse();
        String filename = "商品.xls";
        try {
            // 文件名含有中文，转成原始的字节流，再以iso-8859-1编码形式组装字符串? 因为浏览读取响应头时用的iso-8859-1.
            filename = new String(filename.getBytes(),"iso-8859-1").toString();
            // 告诉浏览器，下载文件
            res.setHeader("Content-Disposition", "attachment;filename=" + filename);
            goodsBiz.exportGoodsTemplate(res.getOutputStream(), getT1());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 导入数据
     */
    public void doImport() {
    	if (null == file) {
    		WebUtil.ajaxReturn(false, "没有选择文件!");
            return;
 		}
        if(!"application/vnd.ms-excel".equals(fileContentType)) {
            if(!fileFileName.endsWith(".xls")) {
                WebUtil.ajaxReturn(false, "文件格式不正确!");
                return;
            }
        }
        try {
            goodsBiz.doImportGoodsTemplate(new FileInputStream(file));
            WebUtil.ajaxReturn(true, "导入成功");
        } catch (IOException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "导入失败");
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "导入失败，发生未知错误，请联系管理员");
        }
    }
    private File file;// input name=file; 文件对象, 大小走过2M，值为null
    private String fileFileName; // 文件名
    private String fileContentType; // 文件的类型

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
