package cn.itcast.erp.biz.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.itcast.erp.biz.IInventoryBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IInventoryDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Inventory;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.exception.ErpException;

/**
 * 盘盈盘亏业务逻辑类
 *
 */
@Service("inventoryBiz")
@SuppressWarnings("unused")
public class InventoryBiz extends BaseBiz<Inventory> implements IInventoryBiz {

    private IInventoryDao inventoryDao;
    @Autowired
    private IStoredetailDao storedetailDao;

    @Resource(name="inventoryDao")
    public void setInventoryDao(IInventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
        super.setBaseDao(this.inventoryDao);
    }
    
    /* 
     * 添加商品名称  审核人 登记人名称
     */
    @Override
    public List<Inventory> getListByPage(Inventory t1, Inventory t2, Object obj, int startRow, int maxResults) {
    	List<Inventory> list = inventoryDao.getListByPage(t1, t2, obj, startRow, maxResults);
    	//遍历赋值
    	for (Inventory inv : list) {
			inv.setCheckerName(getEmpName(inv.getChecker()));
			inv.setCreaterName(getEmpName(inv.getCreater()));
			inv.setGoodsName(goodsDao.get(inv.getGoodsuuid()).getName());
			inv.setStoreName(storeDao.get(inv.getStoreuuid()).getName());
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

	/* 
	 *  盘盈盘亏审核
	 */
	@Override
	@Transactional
	public void doCheck(Long empuuid, Long id) {
	
		//查询出盘盈盘亏记录  持久化状态
		Inventory inventory = inventoryDao.get(id);
		
		//判断订单是否是已审核
		if (Inventory.STATE_CHECK.equals(inventory.getState())) {
			throw new ErpException("该订单已审核过了");
		}
		
		inventory.setChecker(empuuid);
		inventory.setChecktime(new Date());
		inventory.setState(Inventory.STATE_CHECK);
		
		//设置库存操作记录表
		Storeoper so = new Storeoper();
		so.setEmpuuid(empuuid);
		so.setOpertime(new Date());
		so.setStoreuuid(inventory.getStoreuuid());
		so.setGoodsuuid(inventory.getGoodsuuid());
		so.setNum(inventory.getNum());
		//判断是盘盈还是盘亏
		if (Inventory.TYPE_PROFIT.equals(inventory.getType())) {
			//盘盈
			so.setType(Storeoper.TYPE_IN);
		}
		if (Inventory.TYPE_LOSS.equals(inventory.getType())) {
			//盘亏
			so.setType(Storeoper.TYPE_OUT);
		}
		storeoperDao.add(so);
		
		//设置库存明细表 持久化
		Storedetail storedetail = storedetailDao.getStoredetail(inventory.getStoreuuid(), inventory.getGoodsuuid());
		//设置盘点数量
		storedetail.setNum(inventory.getNum());
		
 	}
	
	 //注入empDao
    @Autowired
    private IEmpDao empDao;
    
    //注入storeDao
    @Autowired
    private IStoreDao storeDao;
    
    //注入goodsDao
    @Autowired
    private IGoodsDao goodsDao;
    
   
    //库存变更记录表
    @Autowired
    private IStoreoperDao storeoperDao;
    
    @Override
    @Transactional
    public void add(Inventory inventory) {

        // 查询库存中商品数量
        Storedetail storedetail = new Storedetail();
        storedetail.setGoodsuuid(inventory.getGoodsuuid());
        storedetail.setStoreuuid(inventory.getStoreuuid());

        List<Storedetail> list = storedetailDao.getList(storedetail, null, null);
        if (null != list && list.size() > 0) {
            Storedetail sd = list.get(0);
            Long num = sd.getNum();
            if (num > inventory.getNum()) {
                // 库存商品多，盘亏
                inventory.setType(Inventory.TYPE_LOSS);
            } else if (num < inventory.getNum()) {
                // 库存商品少，盘盈
                inventory.setType(Inventory.TYPE_PROFIT);
            } else {
                throw new ErpException("该商品不需要盘盈盘亏");
            }
        } else {
            throw new ErpException("库存中没有该商品，请检查商品和仓库");
        }

        inventory.setCreatetime(new Date());
        inventory.setState(Inventory.STATE_CREATE);
        // 添加inventory
        super.add(inventory);
    }
    
    
    
    
    
    
}
