package cn.itcast.erp.biz.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.bos.ws.impl.IWaybillWs;

import cn.itcast.erp.biz.IReturnorderdetailBiz;
import cn.itcast.erp.dao.IReturnorderdetailDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Returnorderdetail;
import cn.itcast.erp.entity.Returnorders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;

/**
 * 退货订单明细业务逻辑类
 *
 */
@Service("returnorderdetailBiz")
public class ReturnorderdetailBiz extends BaseBiz<Returnorderdetail> implements IReturnorderdetailBiz {

    private IReturnorderdetailDao returnorderdetailDao;
    @Autowired
    private IStoredetailDao storedetailDao;
    @Autowired
    private IStoreoperDao storeoperDao;
    @Autowired
    private IWaybillWs waybillWs;
    @Autowired
    private ISupplierDao supplierDao;

    @Resource(name="returnorderdetailDao")
    public void setReturnorderdetailDao(IReturnorderdetailDao returnorderdetailDao) {
        this.returnorderdetailDao = returnorderdetailDao;
        super.setBaseDao(this.returnorderdetailDao);
    }
    /**
     * 销售退货入库
     * uuid  商品id
     * storeuuid 仓库id
     * empuuid  当前用户id
     */
    @Override
    @Transactional
    public void doInStore(Long empuuid, Long storeuuid, Long uuid) {
        // 1. 获取明细表returnorderdetail 明细编号,明细的对象进入持久化
    	Returnorderdetail rod = returnorderdetailDao.get(uuid);
        // 1.5 状态判断，如果不是 未入库的 终止
        if (!Returnorderdetail.STATE_NOT_IN_RETURN.equals(rod.getState())) {
            throw new ErpException("亲，该明细已经入库了");
        }
        // 1.1 结束日期 系统时间
        rod.setEndtime(new Date());
        // 1.2 库管员 登陆用户
        rod.setEnder(empuuid);
        // 1.3 仓库编号 前端传过来 提供下拉列表
        rod.setStoreuuid(storeuuid);
        // 1.4 状态 1: 已入库
        rod.setState(Returnorderdetail.STATE_IN_RETURN);

        // 2. 库存表storedetail
        // 2.1 判断是否存在库存信息
        // 根据仓库编号，商品编号 查询库存表 list.size() > 0
        // 构建查询条件
        Storedetail sd = new Storedetail();
        // 查询条件
        sd.setGoodsuuid(rod.getGoodsuuid());
        sd.setStoreuuid(storeuuid);
        List<Storedetail> list = storedetailDao.getList(sd, null, null);
        // 2.2 如果存在库存信息
        if (list.size() > 0) {
            // 数量累加 list.get(0) 库存信息 持久状
            sd = list.get(0);
            // 取出库存的数量 + 明细的数量
            sd.setNum(sd.getNum() + rod.getReturnnum());
        } else {
            // 2.3 不存在库存信息
            // 插入新的记录:
            // 仓库编号 前端传过来
            // 商品编号 明细有
            // 数量 明细的数量
            sd.setNum(rod.getReturnnum());
            storedetailDao.add(sd);
        }
        
        // 3. 日志记录storeoper
        // 插入记录
        Storeoper log = new Storeoper();
        // 3.1 操作员工编号 登陆用户
        log.setEmpuuid(empuuid);
        // 3.2 操作日期 系统时间, 让入库的时间保持一致
        log.setOpertime(rod.getEndtime());
        // 3.3 仓库编号 前端传过来
        log.setStoreuuid(storeuuid);
        // 3.4 商品编号 明细有
        log.setGoodsuuid(rod.getGoodsuuid());
        // 3.5 数量 明细的数量
        log.setNum(rod.getReturnnum());
        // 3.6 操作的类型 1:入库
        log.setType(Storeoper.TYPE_IN);
        storeoperDao.add(log);
        
        // 4. 订单表returnorders, 订单进入持久态
        Returnorders ro = rod.getReturnorders();
        // 4.1 判断订单下的所有明细是否都完成入库
        // 构建查询条件
        Returnorderdetail queryParam = new Returnorderdetail();
        // 查询条件: 订单编号 明细获取, 明细的状态0
        queryParam.setReturnorders(ro);
        queryParam.setState(Returnorderdetail.STATE_NOT_IN_RETURN);
        // 查询订单下的未入库的明细的个数getCount
        Long count = returnorderdetailDao.getCount(queryParam, null, null);
        // 4.2 如果还有明细没有入库count > 0
        // 不需要操作
        // 4.3 如果不存在未入库的明细count = 0
        if (count == 0) {
            // 更新订单:
            // 4.3.1 入库日期： 系统时间
            ro.setEndtime(rod.getEndtime());
            // 4.3.2 库管员 登陆用户
            ro.setEnder(empuuid);
            // 4.3.3 状态 3: 已入库
            ro.setState(Returnorders.STATE_IN_RETURN);
        }
    }
    
	/* 
	 * 采购退货出库
	 */
	@Override
	@Transactional
	public void returnOrderOutStore(Long empuuid, Long storeuuid, Long uuid) {
		//退货订单明细表
    	Returnorderdetail returnorderdetail = returnorderdetailDao.get(uuid);
    	//判断订单详情,防止重复出库
    	if(Returnorderdetail.STATE_OUT.equals(returnorderdetail.getState())){
    		throw new ErpException("已出库,无须重复出库");
    	}
    	//设置库管员的信息
    	returnorderdetail.setEnder(empuuid);
    	//设置订单明细的结束日期为系统时间
    	returnorderdetail.setEndtime(new Date());
    	//设置库管编号
    	returnorderdetail.setStoreuuid(storeuuid);
    	//设置订单明细的状态为已出库
    	returnorderdetail.setState(Returnorderdetail.STATE_OUT);
    	
    	//仓库库存表
    	Storedetail storedetail = new Storedetail();
    	//设置查询条件
    	storedetail.setStoreuuid(storeuuid);
    	storedetail.setGoodsuuid(returnorderdetail.getGoodsuuid());
    	//判断库存信息是否存在
    	List<Storedetail> list = storedetailDao.getList(storedetail, null, null);
    	if(list.size() > 0){
    		//之前有库存信息
    		Storedetail sd = list.get(0);
    		//判断库存是否充足
    		Long num = sd.getNum() - returnorderdetail.getReturnnum();
    		if(num >= 0){
    			sd.setNum(num);
    		}else{
    			throw new ErpException("库存不足");
    		}
    	}else{
    		throw new ErpException("库存不足");
    	}
    	
    	//日志记录表
    	Storeoper storeoper = new Storeoper();
    	//设置操作员工编号
    	storeoper.setEmpuuid(empuuid);
    	//设置操作日期与订单明细结束时间一样
    	storeoper.setOpertime(returnorderdetail.getEndtime());
    	//设置仓库编号
    	storeoper.setStoreuuid(storeuuid);
    	//设置商品
    	storeoper.setGoodsuuid(returnorderdetail.getGoodsuuid());
    	//设置数量
    	storeoper.setNum(returnorderdetail.getReturnnum());
    	//设置操作类型
    	storeoper.setType(Storeoper.TYPE_OUT);
    	storeoperDao.add(storeoper);
    	
    	//退货订单表
		Returnorders returnorder = returnorderdetail.getReturnorders();
		//创建查询条件
		Returnorderdetail rd = new Returnorderdetail();
		rd.setReturnorders(returnorder);
		rd.setState(Returnorderdetail.STATE_NOT_OUT);
		//获取退货明细数量
		Long count = returnorderdetailDao.getCount(rd, null, null);
		//表示退货订单下面明细都已经出货
		if(count == 0){
			//设置退货订单的结束时间
			returnorder.setEndtime(returnorderdetail.getEndtime());
			//设置退货订单的结束人
			returnorder.setEnder(empuuid);
			//设置退货订单状态
			returnorder.setState(Returnorders.STATE_OUT);
			/**
			 * 自动运货下单 by焦凯
			 */
			Supplier sup = supplierDao.get(returnorder.getSupplieruuid());
			Long waybillsn = waybillWs.addWaybill(0L, sup.getAddress(), sup.getName(), sup.getTele(), "快递小哥快过来,我有好东西");
			returnorder.setWaybillsn(waybillsn);
		}
	}
	
}
