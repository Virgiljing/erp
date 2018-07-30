package cn.itcast.erp.biz.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.ISupplierBiz;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;

/**
 * 供应商业务逻辑类
 *
 */
@Service("supplierBiz")
public class SupplierBiz extends BaseBiz<Supplier> implements ISupplierBiz {

    private ISupplierDao supplierDao;

    @Resource(name="supplierDao")
    public void setSupplierDao(ISupplierDao supplierDao) {
        this.supplierDao = supplierDao;
        super.setBaseDao(this.supplierDao);
    }

    @Override
    public void export(OutputStream os, Supplier t1) throws IOException {
        // 得到符合条件的所有数据
        List<Supplier> list = supplierDao.getList(t1, null, null);
        // 创建一个工作簿 HSSF xls
        Workbook wk = new HSSFWorkbook();
        try {
            String shtName = "供应商";
            if(Supplier.TYPE_CUSTOMER.equals(t1.getType())) {
                shtName = "客户";
            }
            // 创建工作表
            Sheet sht = wk.createSheet(shtName);
            // 创建行   表头
            Row row = sht.createRow(0);// 行的下标从0开始
            // 表头内容
            String[] headers = {"名称","地址","联系人","电话","Email"};
            // 宽度
            int[] widths = {4000,8000,2000,3000,8000};
            int i = 0;
            for (; i < headers.length; i++) {
                // 表头
                row.createCell(i).setCellValue(headers[i]);
                sht.setColumnWidth(i, widths[i]);
            }
            // 内容
            i = 1;
            for (Supplier supplier : list) {
                if(null == supplier) {
                    continue;
                }
                row = sht.createRow(i);
                row.createCell(0).setCellValue(supplier.getName());
                row.createCell(1).setCellValue(supplier.getAddress());
                row.createCell(2).setCellValue(supplier.getContact());
                row.createCell(3).setCellValue(supplier.getTele());
                row.createCell(4).setCellValue(supplier.getEmail());
                i++;
            }
            // 保存到输出流
            wk.write(os);
        } finally {
            try {
                wk.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    @Transactional
    public void doImport(InputStream is) throws IOException {
        // 读取工作簿
        Workbook wk = new HSSFWorkbook(is);
        try {
            // 读取第一个工作表
            Sheet sht = wk.getSheetAt(0);

            String sheetName = sht.getSheetName();
            String type = Supplier.TYPE_CUSTOMER;
            if("供应商".equals(sheetName)) {
                type = Supplier.TYPE_SUPPLIER;
            }

            // 最后一行的下标
            Row row = null;
            String name = null;
            Supplier supplier = null;
            List<Supplier> list = null;
            // i=1是要忽略表头行
            for(int i = 1; i <= sht.getLastRowNum(); i++) {
                row = sht.getRow(i);
                // 供应商名称
                name = row.getCell(0).getStringCellValue();
                supplier = new Supplier();
                supplier.setName(name);
                // 判断是否存在
                list = supplierDao.getList(null, supplier, null);
                if(list.size() > 0) {
                    // 存在
                    supplier = list.get(0); // 进入持久化状
                }
                supplier.setAddress(row.getCell(1).getStringCellValue());
                //row.getCell(2).getCellType 判断单元格的数据类型
                supplier.setContact(row.getCell(2).getStringCellValue());
                supplier.setTele(row.getCell(3).getStringCellValue());
                supplier.setEmail(row.getCell(4).getStringCellValue());
                if(list.size() == 0) {
                    //设置类型
                    supplier.setType(type);
                    //不存在
                    supplierDao.add(supplier);
                }
            }
        } finally {
            try {
                wk.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    @Transactional
    public void add(Supplier t) {
        Subject subject = SecurityUtils.getSubject();
        if(Supplier.TYPE_SUPPLIER.equals(t.getType())) {
            if(!subject.isPermitted("供应商")) {
                throw new ErpException("没有权限");
            }
        }else if(Supplier.TYPE_CUSTOMER.equals(t.getType())) {
            if(!subject.isPermitted("客户")) {
                throw new ErpException("没有权限");
            }
        }else {
            throw new ErpException("提交的参数异常");
        }

        super.add(t);
    }

}
