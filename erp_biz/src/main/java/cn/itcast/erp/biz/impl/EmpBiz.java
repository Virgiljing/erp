package cn.itcast.erp.biz.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IEmpBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IRoleDao;
import cn.itcast.erp.dao.impl.DepDao;
import cn.itcast.erp.entity.Dep;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Menu;
import cn.itcast.erp.entity.Role;
import cn.itcast.erp.entity.Tree;
import cn.itcast.erp.exception.ErpException;
import net.sf.jxls.transformer.XLSTransformer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 员工业务逻辑类
 *
 */
@Service("empBiz")
public class EmpBiz extends BaseBiz<Emp> implements IEmpBiz {
	@Autowired
	private DepDao depDao;

	private IEmpDao empDao;
	/** 散列次数 3 */
	private int hashIteration = 3;
	@Autowired
	private IRoleDao roleDao;

	@Autowired
	private JedisPool jedisPool;

	@Resource(name = "empDao")
	public void setEmpDao(IEmpDao empDao) {
		this.empDao = empDao;
		super.setBaseDao(this.empDao);
	}

	@Override
	public Emp findByUsernameAndPwd(String username, String pwd) {
		// source: 要加密的内容
		// salt: 盐, 扰乱码
		// hashIteration： 散列次数 3
		Md5Hash md5 = new Md5Hash(pwd, username, 3);
		pwd = md5.toString();// 加密后的密码
		System.out.println("pwd:" + pwd);
		return empDao.findByUsernameAndPwd(username, pwd);
	}

	@Override
	@Transactional
	public void add(Emp t) {
		// 把密码加密
		String newPwd = encrypt(t.getUsername(), t.getUsername());
		// 设置成加密后的密码
		t.setPwd(newPwd);
		super.add(t);
	}

	/**
	 * 加密
	 * 
	 * @param src
	 *            要加密的密码
	 * @param salt
	 *            username做为盐
	 * @return
	 */
	private String encrypt(String src, String salt) {
		Md5Hash md5 = new Md5Hash(src, salt, hashIteration);
		return md5.toString();// 加密后的密码
	}

	@Override
	@Transactional
	public void updatePwd(String oldPwd, String newPwd, Long uuid) {
		Emp emp = empDao.get(uuid);
		// 原密码进行加密
		oldPwd = encrypt(oldPwd, emp.getUsername());
		if (!oldPwd.equals(emp.getPwd())) {
			throw new ErpException("原密码不正确");
		}
		// 加密新密码
		newPwd = encrypt(newPwd, emp.getUsername());
		// 更新密码
		empDao.updatePwd(newPwd, uuid);
	}

	@Override
	@Transactional
	public void updatePwd_reset(String newPwd, Long uuid) {
		Emp emp = empDao.get(uuid);
		newPwd = encrypt(newPwd, emp.getUsername());
		// 更新密码
		empDao.updatePwd(newPwd, uuid);
	}

	@Override
	public List<Tree> readEmpRoles(Long uuid) {
		List<Tree> result = new ArrayList<Tree>();
		// 获取用户信息，进入持久态
		Emp emp = empDao.get(uuid);
		// 得到 用户所拥有的角色, 进入持久态
		List<Role> empRoles = emp.getRoles();
		// 角色列表
		List<Role> list = roleDao.getList(null, null, null);
		// 把角色转成树的节点
		for (Role role : list) {
			Tree tree = new Tree();
			tree.setId(role.getUuid() + "");
			tree.setText(role.getName());
			// 判断用户是否拥有这个角色
			if (empRoles.contains(role)) {
				// 让它选中
				tree.setChecked(true);
			}
			result.add(tree);
		}
		return result;
	}

	@Override
	@Transactional
	public void updateEmpRoles(Long uuid, String ids) {
		// 获取用户，进入持久化
		Emp emp = empDao.get(uuid);
		// 清空用户下的所有角色，delete from emp_role where empuuid=?
		emp.setRoles(new ArrayList<Role>());

		// 建立新的关系
		String[] roleIds = ids.split(",");
		for (String roleId : roleIds) {
			Role role = roleDao.get(Long.valueOf(roleId));
			emp.getRoles().add(role);
		}

		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			// 清空用户的权限缓存
			jedis.del(Menu.MENUS_KEY + uuid);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				jedis.close();
			}
			jedis = null;
		}
	}

	/*
	 * 导出到excel文件
	 * 
	 * @param os 输出流
	 */
	@Override
	public void export(OutputStream os,Emp t1) {
		  List<Emp> empList = empDao.getList(t1, null, null);
		  for (Emp emp : empList) {
			emp.setSex("男");
			if(emp.getGender()==0l){
				emp.setSex("女");
			}
		}
	        // 创建一个工作簿 HSSF xls
	        Workbook wk = null;
	        try {
	            // 加载模板文件
	            try {
					wk = new HSSFWorkbook(new ClassPathResource("template/temps.xls").getInputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

	            //创建数据模型
	            Map<Object,Object> dataModel = new HashMap<Object,Object>();
	            dataModel.put("emp", empList);
	            // 转换器
	            XLSTransformer transformer = new XLSTransformer();
	            // 把数据模型中的数据填充到模板文件中
	            transformer.transformWorkbook(wk, dataModel);
	            try {
					wk.write(os);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        } finally {
	            try {
	                if(null != wk) {
	                    wk.close();
	                }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }

	    }

	    @SuppressWarnings("unused")
		private void setDateValue(Cell cell, Date date) {
	        if(null != date) {
	            cell.setCellValue(date);
	        }
	    }

		
	
/*		// 写入表头
		HSSFRow row = sheet.createRow(0);
		// 定义每一列的标题
		String[] headerNames = { "登录名", "真实姓名", "性别", "邮件地址", "联系电话", "联系地址", "出生年月日", "部门" };
		// 指定每一列的宽度
		int[] columnWidths = { 3500, 3000, 1500, 6000, 5000, 7000, 5000, 3000 };
		HSSFCell cell = null;
		for (int i = 0; i < headerNames.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(headerNames[i]);
			// 设置每列的宽度
			sheet.setColumnWidth(i, columnWidths[i]);
		}
		// 写入内容
		int i = 1;
		for (Emp emp : empList) {
			String gender = "男";
			row = sheet.createRow(i);
			// 按照hderarNames的顺序来
			row.createCell(0).setCellValue(emp.getUsername());// 登录名
			row.createCell(1).setCellValue(emp.getName());// 真实姓名
			if (emp.getGender() == 0) {
				gender = "女";
			}
			row.createCell(2).setCellValue(gender);// 性别
			row.createCell(3).setCellValue(emp.getEmail());// 邮件地址
			row.createCell(4).setCellValue(emp.getTele());// 联系电话
			row.createCell(5).setCellValue(emp.getAddress());// 联系地址
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			row.createCell(6).setCellValue(df.format(emp.getBirthday()));// 出生年月日
			row.createCell(7).setCellValue(emp.getDep().getName());// 部门
			i++;
		}
		try {
			//写入到输出流中
			wk.write(os);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
			 //关闭工作簿
				wk.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/



	/* 
	 * 员工数据导入
	 */
	@Transactional
	@Override
	public void doExport(InputStream is) throws IOException {
    Long gender=1L;//申明默认性别为男性
	HSSFWorkbook wb=null;
	try { 
		
		wb=new HSSFWorkbook(is);
	HSSFSheet sheet = wb.getSheetAt(0);
//登录名", "真实姓名", "性别", "邮件地址", "联系电话,"联系地址", "出生年月日", "部门" 
	//读取数据（获得最后一行的行号）
	int lastRow=sheet.getLastRowNum();
	Emp emp=null;
	for(int i=1;i<=lastRow;i++){
		emp=new Emp();
		if(sheet.getRow(i).getCell(0)==null){
			break;
		}
		emp.setUsername(sheet.getRow(i).getCell(0).getStringCellValue());//设置登录名
		//判断员工是否存在，通过登录名判断
		List<Emp> list = empDao.getList(emp,null,null);
		if(list.size()>0){
			emp=list.get(0);
		}
		emp.setName(sheet.getRow(i).getCell(1).getStringCellValue());//设置真实姓名
		emp.setGender(gender); //设置性别
		if(sheet.getRow(i).getCell(2).getStringCellValue().equals("女")){
			//gender=0L; //要导入的数据是女性
			emp.setGender(0L);
		}
		emp.setGender(gender); //设置性别
		emp.setEmail(sheet.getRow(i).getCell(3).getStringCellValue());//设置邮件地址
		emp.setTele(sheet.getRow(i).getCell(4).getStringCellValue());//设置电话
		emp.setAddress(sheet.getRow(i).getCell(5).getStringCellValue());//设置地址
		emp.setBirthday((sheet.getRow(i).getCell(6).getDateCellValue()));
		String depName = sheet.getRow(i).getCell(7).getStringCellValue();
		Dep dep=new Dep();
		dep.setName(depName);
		dep = depDao.getList(null, dep, null).get(0);
		emp.setDep(dep);//设置部门
		String pwd = encrypt("123456",sheet.getRow(i).getCell(0).getStringCellValue());
		 emp.setPwd(pwd);
		if(list.size()==0){
			//新增
			empDao.add(emp);	
		}
	}
		
	}finally{
		if(null!=wb){
			try {
				wb.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	}
}


	
		

