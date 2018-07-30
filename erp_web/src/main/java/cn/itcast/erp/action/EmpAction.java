package cn.itcast.erp.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.util.WebUtil;

@Controller("empAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("emp")
public class EmpAction extends BaseAction<Emp> {

	private File file;// input name=file;
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

	private IEmpBiz empBiz;
	private String oldPwd;// 原密码
	private String newPwd;// 新密码
	private String ids;// 角色编号，多个以逗号分割

	public void setOldPwd(String oldPwd) {
		this.oldPwd = oldPwd;
	}

	public void setNewPwd(String newPwd) {
		this.newPwd = newPwd;
	}

	@Resource(name = "empBiz")
	public void setEmpBiz(IEmpBiz empBiz) {
		this.empBiz = empBiz;
		super.setBaseBiz(this.empBiz);
	}

	/**
	 * 修改密码
	 */
	public void updatePwd() {
		Emp loginUser = WebUtil.getLoginUser();
		if (null == loginUser) {
			WebUtil.ajaxReturn(false, "你还没有登陆");
			return;
		}
		try {
			empBiz.updatePwd(oldPwd, newPwd, loginUser.getUuid());
			WebUtil.ajaxReturn(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "修改失败");
		}
	}

	/**
	 * 重置密码
	 */
	public void updatePwd_reset() {
		try {
			empBiz.updatePwd_reset(newPwd, getId());
			WebUtil.ajaxReturn(true, "重置密码成功");
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "重置密码失败");
		}
	}

	/**
	 * 获取用户角色设置信息
	 */
	public void readEmpRoles() {
		List<Tree> list = empBiz.readEmpRoles(getId());
		WebUtil.write(list);
	}

	/**
	 * 设置用户角色
	 */
	public void updateEmpRoles() {
		try {
			empBiz.updateEmpRoles(getId(), ids);
			WebUtil.ajaxReturn(true, "更新成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "更新失败");
		}
	}

	public void setIds(String ids) {
		this.ids = ids;
	}

	/**
	 * 员工信息的导出
	 */
	public void export() {
		String filename = "员工信息.xls";
		HttpServletResponse response = ServletActionContext.getResponse();
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1"));// 中文名称进行转码
			// 调用导出业务
			empBiz.export(response.getOutputStream(),getT1());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 员工信息的导入
	 */
	/**
	 * 导入数据
	 */
	public void doImport() {
		if (null == file) {
			WebUtil.ajaxReturn(false, "请选择文件!");
			return;
		}
		if (!"application/vnd.ms-excel".equals(fileContentType)) {
			if (!fileFileName.endsWith(".xls")) {
				WebUtil.ajaxReturn(false, "文件格式不正确!");
				return;
			}
		}
		try {
			empBiz.doExport(new FileInputStream(file));
			WebUtil.ajaxReturn(true, "导入成功");
		} catch (IOException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "导入失败");
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "导入失败，发生未知错误，请联系管理员");
		}
	}

}
