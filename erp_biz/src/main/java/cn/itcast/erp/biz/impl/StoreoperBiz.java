package cn.itcast.erp.biz.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.itcast.erp.biz.IStoreoperBiz;
import cn.itcast.erp.dao.IEmpDao;
import cn.itcast.erp.dao.IGoodsDao;
import cn.itcast.erp.dao.IStoreDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.entity.Storeoper;

/**
 * 仓库操作记录业务逻辑类
 *
 */
@Service("storeoperBiz")
public class StoreoperBiz extends BaseBiz<Storeoper> implements IStoreoperBiz {

    private IStoreoperDao storeoperDao;

    @Autowired
    private IGoodsDao goodsDao;
    @Autowired
    private IStoreDao storeDao;
    @Autowired
    private IEmpDao empDao;

    @Resource(name="storeoperDao")
    public void setStoreoperDao(IStoreoperDao storeoperDao) {
        this.storeoperDao = storeoperDao;
        super.setBaseDao(this.storeoperDao);
    }

    @Override
    public List<Storeoper> getListByPage(Storeoper t1, Storeoper t2, Object obj, int startRow, int maxResults) {
        List<Storeoper> list = super.getListByPage(t1, t2, obj, startRow, maxResults);
        for (Storeoper storeoper : list) {
            storeoper.setEmpName(empDao.get(storeoper.getEmpuuid()).getName());
            storeoper.setGoodsName(goodsDao.get(storeoper.getGoodsuuid()).getName());
            storeoper.setStoreName(storeDao.get(storeoper.getStoreuuid()).getName());
        }
        return list;
    }

}
