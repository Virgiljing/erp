package cn.itcast.erp.biz.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IStoreBiz;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.entity.Store;

/**
 * 仓库业务逻辑类
 *
 */
@Service("storeBiz")
public class StoreBiz extends BaseBiz<Store> implements IStoreBiz {

    private IStoreDao storeDao;

    @Resource(name="storeDao")
    public void setStoreDao(IStoreDao storeDao) {
        this.storeDao = storeDao;
        super.setBaseDao(this.storeDao);
    }
    
}
