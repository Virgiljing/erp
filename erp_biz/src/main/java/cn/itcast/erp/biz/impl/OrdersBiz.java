package cn.itcast.erp.biz.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.erp.dao.impl.StoredetailDao;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IOrdersDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.exception.ErpException;

/**
 * 订单业务逻辑类
 *
 */
@Service("ordersBiz")
public class OrdersBiz extends BaseBiz<Orders> implements IOrdersBiz {

    private IOrdersDao ordersDao;
    @Autowired
    private IEmpDao empDao;

    @Autowired
    private ISupplierDao supplierDao;

    @Resource(name="ordersDao")
    public void setOrdersDao(IOrdersDao ordersDao) {
        this.ordersDao = ordersDao;
        super.setBaseDao(this.ordersDao);
    }

    @Override
    @Transactional
    public void add(Orders orders) {
        //        1.1 生成日期   系统时间
        orders.setCreatetime(new Date());
        //        1.2 订单类型   1
        // orders.setType(Orders.TYPE_IN); 类型由对应的action来决定
        //        1.6 订单状态   0:未审核

        String detailState = Orderdetail.STATE_NOT_IN;

        Subject subject = SecurityUtils.getSubject();
        if(Orders.TYPE_OUT.equals(orders.getType())) {
            if(!subject.isPermitted("我的销售订单")) {
                throw new ErpException("没有权限");
            }
            // 销售
            detailState = Orderdetail.STATE_NOT_OUT;
        }else if(Orders.TYPE_IN.equals(orders.getType())) {
            if(!subject.isPermitted("我的采购订单")) {
                throw new ErpException("没有权限");
            }
        }else {
            throw new ErpException("提交的参数异常");
        }
        // 循环所有明细进行累加
        double totalmoney = 0;
        for(Orderdetail od : orders.getOrderDetails()) {
            totalmoney += od.getMoney();
            // 明细的状态
            od.setState(detailState);
            // 设置明细与订单的关系, 在插入明细时，把订单的编号交给明细
            od.setOrders(orders);
        }
        //      1.5 合计金额   通过明细累计
        orders.setTotalmoney(totalmoney);
        ordersDao.add(orders);
    }

    @Override
    public List<Orders> getListByPage(Orders t1, Orders t2, Object obj, int startRow, int maxResults) {
        List<Orders> list = super.getListByPage(t1, t2, obj, startRow, maxResults);
        // 循环赋值名称
        for (Orders orders : list) {
            orders.setCreaterName(empDao.get(orders.getCreater()).getName());
            orders.setCheckerName(getEmpName(orders.getChecker()));
            orders.setStarterName(getEmpName(orders.getStarter()));
            orders.setEnderName(getEmpName(orders.getEnder()));

            orders.setSupplierName(supplierDao.get(orders.getSupplieruuid()).getName());

            List<Orderdetail> orderDetails = orders.getOrderDetails();
            if (orderDetails.size() > 0) {
                for (Orderdetail orderDetail : orderDetails) {
                    Long goodsuuid = orderDetail.getGoodsuuid();
                    Long storeNum = storedetailDao.findStoreNumByGoodsuuidNotStoreuuid(goodsuuid);
                    orderDetail.setStoreNum(storeNum);
                }
            }
        }
        return list;
    }

    /**
     * 获取员工名称
     * @param uuid
     * @return
     */
    private String getEmpName(Long uuid) {
        if(null == uuid) {
            return null;
        }
        return empDao.get(uuid).getName();
    }

    @Override
    @Transactional
    public void doCheck(Long empuuid, Long uuid) {
        // 获取订单对象，进入持久化状态, 事务一提交就会自动更新到数据库
        Orders orders = ordersDao.get(uuid);
        // 判断订单的状态
        if(!Orders.STATE_CREATE.equals(orders.getState())) {
            // 不是未审核状态
            throw new ErpException("亲，该订单已经审核过了");
        }
        // 审核日期
        orders.setChecktime(new Date());
        // 订单状态
        orders.setState(Orders.STATE_CHECK);
        // 审核人
        orders.setChecker(empuuid);
    }

    @Override
    @Transactional
    public void doStart(Long empuuid, Long uuid) {
        // 获取订单对象，进入持久化状态, 事务一提交就会自动更新到数据库
        Orders orders = ordersDao.get(uuid);
        // 判断订单的状态
        if(!Orders.STATE_CHECK.equals(orders.getState())) {
            // 不是未确认状态
            throw new ErpException("亲，该订单已经确认过了");
        }
        // 确认日期
        orders.setStarttime(new Date());
        // 订单状态
        orders.setState(Orders.STATE_START);
        // 确认人
        orders.setStarter(empuuid);
    }

    @Override
    public void exportById(OutputStream os, Long uuid) throws IOException {
        Orders orders = ordersDao.get(uuid);
        List<Orderdetail> orderDetails = orders.getOrderDetails();
        // 创建一个工作簿 HSSF xls
        Workbook wk = new HSSFWorkbook();
        try {
            String title = "采 购 单";
            if(Orders.TYPE_OUT.equals(orders.getType())) {
                title = "销 售 单";
            }
            // 创建工作表
            Sheet sht = wk.createSheet(title);

            // 创建样式
            CellStyle style_content = wk.createCellStyle();
            // 标题的样式
            CellStyle style_title = wk.createCellStyle();

            // 设置水平居中
            style_content.setAlignment(CellStyle.ALIGN_CENTER);
            // 垂直居中
            style_content.setVerticalAlignment(CellStyle.VERTICAL_CENTER);

            // 复制了style_content的样式，格式刷
            style_title.cloneStyleFrom(style_content);

            // 字体设置
            Font font_content = wk.createFont();
            font_content.setFontName("宋体");
            font_content.setFontHeightInPoints((short)11);// 字体的大小
            style_content.setFont(font_content);

            Font font_title = wk.createFont();
            font_title.setFontName("黑体");
            font_title.setFontHeightInPoints((short)18);// 字体的大小
            style_title.setFont(font_title);

            // 设置边框
            style_content.setBorderLeft(CellStyle.BORDER_THIN); // 左边细边框
            style_content.setBorderRight(CellStyle.BORDER_THIN); // 右边细边框
            style_content.setBorderTop(CellStyle.BORDER_THIN); // 上边细边框
            style_content.setBorderBottom(CellStyle.BORDER_THIN); // 下边细边框

            // 日期的样式
            CellStyle style_date = wk.createCellStyle();
            style_date.cloneStyleFrom(style_content);
            DataFormat dateFormat = wk.createDataFormat();
            style_date.setDataFormat(dateFormat.getFormat("yyyy-MM-dd HH:mm"));


            // 合并区域
            sht.addMergedRegion(new CellRangeAddress(0,0,0,3)); // 标题
            sht.addMergedRegion(new CellRangeAddress(2,2,1,3)); // 供应商
            sht.addMergedRegion(new CellRangeAddress(7,7,0,3));


            // 标题
            Row row = sht.createRow(0);
            row.setHeight((short)1000);

            Cell cell_title = row.createCell(0);
            cell_title.setCellValue(title);
            cell_title.setCellStyle(style_title);

            int i = 2;
            int rowCount = 10 + orderDetails.size();
            for(; i < rowCount; i++) {
                row = sht.createRow(i); // 12行
                row.setHeight((short)500);
                for(int j = 0; j < 4; j++) {
                    // 设置样式
                    row.createCell(j).setCellStyle(style_content);;// 四列
                }
            }

            // 列宽
            for(int col = 0; col < 4; col++) {
                sht.setColumnWidth(col, 5000);
            }

            // 设置固定的值
            sht.getRow(2).getCell(0).setCellValue("供应商");
            sht.getRow(2).getCell(1).setCellValue(supplierDao.get(orders.getSupplieruuid()).getName());


            sht.getRow(3).getCell(0).setCellValue("下单日期");
            sht.getRow(4).getCell(0).setCellValue("审核日期");
            sht.getRow(5).getCell(0).setCellValue("采购日期");
            sht.getRow(6).getCell(0).setCellValue("入库日期");

            setDateValue(sht.getRow(3).getCell(1),orders.getCreatetime());
            setDateValue(sht.getRow(4).getCell(1),orders.getChecktime());
            setDateValue(sht.getRow(5).getCell(1),orders.getStarttime());
            setDateValue(sht.getRow(6).getCell(1),orders.getEndtime());

            sht.getRow(3).getCell(2).setCellValue("经办人");
            sht.getRow(4).getCell(2).setCellValue("经办人");
            sht.getRow(5).getCell(2).setCellValue("经办人");
            sht.getRow(6).getCell(2).setCellValue("经办人");

            sht.getRow(3).getCell(3).setCellValue(getEmpName(orders.getCreater()));
            sht.getRow(4).getCell(3).setCellValue(getEmpName(orders.getChecker()));
            sht.getRow(5).getCell(3).setCellValue(getEmpName(orders.getStarter()));
            sht.getRow(6).getCell(3).setCellValue(getEmpName(orders.getEnder()));

            sht.getRow(7).getCell(0).setCellValue("订单明细");

            sht.getRow(8).getCell(0).setCellValue("商品名称");
            sht.getRow(8).getCell(1).setCellValue("数量");
            sht.getRow(8).getCell(2).setCellValue("价格");
            sht.getRow(8).getCell(3).setCellValue("金额");

            // 设置日期格式
            sht.getRow(3).getCell(1).setCellStyle(style_date);
            sht.getRow(4).getCell(1).setCellStyle(style_date);
            sht.getRow(5).getCell(1).setCellStyle(style_date);
            sht.getRow(6).getCell(1).setCellStyle(style_date);

            // 设置明细的值
            i = 9;
            for (Orderdetail od : orderDetails) {
                row = sht.getRow(i);
                row.getCell(0).setCellValue(od.getGoodsname());
                row.getCell(1).setCellValue(od.getNum());
                row.getCell(2).setCellValue(od.getPrice());
                row.getCell(3).setCellValue(od.getMoney());
                i++;
            }

            sht.getRow(i).getCell(0).setCellValue("合计");
            sht.getRow(i).getCell(3).setCellValue(orders.getTotalmoney());


            // 保存到本地
            wk.write(os);
        } finally {
            try {
                wk.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void setDateValue(Cell cell, Date date) {
        if(null != date) {
            cell.setCellValue(date);
        }
    }

    @Autowired
    private StoredetailDao storedetailDao;
}
