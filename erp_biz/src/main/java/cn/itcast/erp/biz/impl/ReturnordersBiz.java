package cn.itcast.erp.biz.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IReturnordersBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IReturnorderdetailDao;
import cn.itcast.erp.dao.IReturnordersDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Returnorderdetail;
import cn.itcast.erp.entity.Returnorders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.biz.ISupplierBiz;
/**
 * 退货订单业务逻辑类
 *
 */
@Service("returnordersBiz")
public class ReturnordersBiz extends BaseBiz<Returnorders> implements IReturnordersBiz {

    private IReturnordersDao returnordersDao;
    @Autowired
    private IReturnorderdetailDao returnorderdetailDao;
    @Autowired
    private IEmpDao empDao;
    @Autowired
    private ISupplierDao supplierDao;
    @Autowired
    private IStoredetailDao storedetailDao;
    
    @Autowired
    private ISupplierBiz supplierBiz;
    @Resource(name="returnordersDao")
    public void setReturnordersDao(IReturnordersDao returnordersDao) {
        this.returnordersDao = returnordersDao;
        super.setBaseDao(this.returnordersDao);
    }
    @Override

    public List<Returnorders> getListByPage(Returnorders t1, Returnorders t2, Object obj, int
            startRow, int maxResults) {
        List<Returnorders> list = super.getListByPage(t1, t2, obj, startRow, maxResults);
        // 循环赋值名称
        for (Returnorders returnorders : list) {
            returnorders.setCreaterName(empDao.get(returnorders.getCreater()).getName());
            returnorders.setCheckerName(getEmpName(returnorders.getChecker()));
            returnorders.setEnderName(getEmpName(returnorders.getEnder()));
            // returnorders.setSupplierName(getEmpName(returnorders.getSupplieruuid()));
            returnorders.setSupplierName(supplierDao.get(returnorders.getSupplieruuid()).getName());
        }
        return list;
    }

    /* 
     * 采购退货登记
     */
    @Override
    @Transactional
    public void add(Returnorders returnorders) {
        //        1.1 生成日期   系统时间
        returnorders.setCreatetime(new Date());
        //        1.2 订单类型   1
        // returnorders.setType(Orders.TYPE_IN); 类型由对应的action来决定
        //        1.6 订单状态   0:未审核

        String detailState = Returnorderdetail.STATE_NOT_IN_RETURN;

//        Subject subject = SecurityUtils.getSubject();
//        if(Returnorders.TYPE_OUT.equals(returnorders.getType())) {
//            if(!subject.isPermitted("采购退货登记")) {
//                throw new ErpException("没有权限");
//            }
//            // 销售
//            detailState = Orderdetail.STATE_NOT_OUT;
//        }else if(Returnorders.TYPE_IN.equals(returnorders.getType())) {
//            if(!subject.isPermitted("销售退货登记")) {
//                throw new ErpException("没有权限");
//            }
//        }else {
//            throw new ErpException("提交的参数异常");
//        }
        // 循环所有明细进行累加
        double totalmoney = 0;
        List<Returnorderdetail> list = returnorders.getReturnorderdetails();
        List<Returnorderdetail> listcopy= new ArrayList<>();
        for (Returnorderdetail rd : list) {
            Storedetail storedetail = new Storedetail();
            storedetail.setGoodsuuid(rd.getGoodsuuid());
            List<Storedetail> storedetails = storedetailDao.getList(storedetail, null, null);
            for (Storedetail sd : storedetails) {

                if (sd.getNum() > rd.getReturnnum()) {
                    sd.setNum(sd.getNum() - rd.getReturnnum());
                } else {
                    throw new ErpException("库存数量不足");
                }
                storedetailDao.update(sd);

            }
            totalmoney += rd.getReturnnum() * rd.getPrice();
            // 明细的状态
            rd.setState(detailState);
            rd.setMoney(rd.getReturnnum() * rd.getPrice());
            // 设置明细与订单的关系, 在插入明细时，把订单的编号交给明细
            if (rd.getReturnnum() != 0) {
                listcopy.add(rd);
            }
            returnorders.setReturnorderdetails(listcopy);
            rd.setReturnorders(returnorders);
        }
        returnorders.setTotalmoney(totalmoney);
        returnordersDao.add(returnorders);
    }
   
	/* 
	 *采购退货审核 
	 */
	@Override
	@Transactional
	public void type1outGoodsDoCheck(Long logUserUuid, Long id) {
		//获取退货订单
		Returnorders returnorders = returnordersDao.get(id);
		//如果不是未审核
		if(!Returnorders.STATE_CREATE.equals(returnorders.getState())) {
			throw new ErpException("亲，该订单已经审核过了");
		}
		returnorders.setChecktime(new Date());
	        // 订单状态
		returnorders.setState(Returnorders.STATE_CHECK);
		returnorders.setChecker(logUserUuid);
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
    /*
     * 销售退货订单审核
     */
	@Override
	@Transactional
	public void doCheck(Long uuid, Long id) {
		Returnorders returnorders = returnordersDao.get(id);
		if(!Returnorders.STATE_CREATE.equals(returnorders.getState())){
			throw new ErpException("亲，您的销售退货订单已审核过！");
		}
		returnorders.setChecker(uuid);
		
		returnorders.setChecktime(new Date());
		returnorders.setCreaterName(getEmpName(returnorders.getCreater()));
		returnorders.setCheckerName(getEmpName(returnorders.getChecker()));
		returnorders.setEnderName(getEmpName(returnorders.getEnder()));
		
		returnorders.setState(Returnorders.STATE_CHECK);
	}

	
	
	
    
    
	/* 
	 * 销售退单新增登记   
	 * 赵萌
	 */
	@Override
	@Transactional
	public void addSaleReturnOrder(Returnorders returnorders) {
		//添加客户,总金额与订单状态的信息
    	//订单状态
    	returnorders.setState(Returnorders.STATE_CREATE);
    	returnorders.setType(Returnorders.TYPE_OUT);
    	//添加客户
    	Supplier supplier = supplierBiz.get(returnorders.getSupplieruuid());
    	returnorders.setSupplierName(supplier.getName());
    	//总金额
    	double total = 0.0;
    	for (Returnorderdetail rd : returnorders.getReturnorderdetails()) {
			total+=rd.getMoney();
			//设置订单明细状态为未入库
			rd.setState(Returnorderdetail.STATE_NOT_IN_RETURN);
            // 设置明细与订单的关系, 在插入明细时，把订单的编号交给明细
            rd.setReturnorders(returnorders);
            returnorderdetailDao.add(rd);
        }
        returnorders.setTotalmoney(total);
        //订单新增
        returnordersDao.add(returnorders);
		
	}
	
}
