package cn.itcast.erp.biz.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.bos.ws.impl.IWaybillWs;

import cn.itcast.erp.biz.IOrderdetailBiz;
import cn.itcast.erp.dao.IOrderdetailDao;
import cn.itcast.erp.dao.IStoredetailDao;
import cn.itcast.erp.dao.IStoreoperDao;
import cn.itcast.erp.dao.ISupplierDao;
import cn.itcast.erp.entity.Orderdetail;
import cn.itcast.erp.entity.Orders;
import cn.itcast.erp.entity.Storedetail;
import cn.itcast.erp.entity.Storeoper;
import cn.itcast.erp.entity.Supplier;
import cn.itcast.erp.exception.ErpException;

/**
 * 订单明细业务逻辑类
 *
 */
@Service("orderdetailBiz")
public class OrderdetailBiz extends BaseBiz<Orderdetail> implements IOrderdetailBiz {

    private IOrderdetailDao orderdetailDao;
    @Autowired
    private IStoredetailDao storedetailDao;
    @Autowired
    private IStoreoperDao storeoperDao;

    @Autowired
    private IWaybillWs waybillWs;

    @Autowired
    private ISupplierDao supplierDao;

    @Resource(name = "orderdetailDao")
    public void setOrderdetailDao(IOrderdetailDao orderdetailDao) {
        this.orderdetailDao = orderdetailDao;
        super.setBaseDao(this.orderdetailDao);
    }

    @Override
    @Transactional
    public void doInStore(Long empuuid, Long storeuuid, Long uuid) {
        // 1. 明细表orderdetail 明细编号, 明细的对象进入持久化
        Orderdetail od = orderdetailDao.get(uuid);
        // 1.5 状态判断，如果不是 未库入库的 终止
        if (!Orderdetail.STATE_NOT_IN.equals(od.getState())) {
            throw new ErpException("亲，该明细已经入库了");
        }
        // 1.1 结束日期 系统时间
        od.setEndtime(new Date());
        // 1.2 库管员 登陆用户
        od.setEnder(empuuid);
        // 1.3 仓库编号 前端传过来 提供下拉列表
        od.setStoreuuid(storeuuid);
        // 1.4 状态 1: 已入库
        od.setState(Orderdetail.STATE_IN);

        // 2. 库存表storedetail
        // 2.1 判断是否存在库存信息
        // 根据仓库编号，商品编号 查询库存表 list.size() > 0
        // 构建查询条件
        Storedetail sd = new Storedetail();
        // 查询条件
        sd.setGoodsuuid(od.getGoodsuuid());
        sd.setStoreuuid(storeuuid);
        List<Storedetail> list = storedetailDao.getList(sd, null, null);
        // 2.2 如果存在库存信息
        if (list.size() > 0) {
            // 数量累加 list.get(0) 库存信息 持久状
            sd = list.get(0);
            // 取出库存的数量 + 明细的数量
            sd.setNum(sd.getNum() + od.getNum());
        } else {
            // 2.3 不存在库存信息
            // 插入新的记录:
            // 仓库编号 前端传过来
            // 商品编号 明细有
            // 数量 明细的数量
            sd.setNum(od.getNum());
            storedetailDao.add(sd);
        }
        // 3. 日志记录storeoper
        // 插入记录
        Storeoper log = new Storeoper();
        // 3.1 操作员工编号 登陆用户
        log.setEmpuuid(empuuid);
        // 3.2 操作日期 系统时间, 让入库的时间保持一致
        log.setOpertime(od.getEndtime());
        // 3.3 仓库编号 前端传过来
        log.setStoreuuid(storeuuid);
        // 3.4 商品编号 明细有
        log.setGoodsuuid(od.getGoodsuuid());
        // 3.5 数量 明细的数量
        log.setNum(od.getNum());
        // 3.6 操作的类型 1:入库
        log.setType(Storeoper.TYPE_IN);
        storeoperDao.add(log);
        // 4. 订单表orders, 订单进入持久态
        Orders orders = od.getOrders();
        // 4.1 判断订单下的所有明细是否都完成入库
        // 构建查询条件
        Orderdetail queryParam = new Orderdetail();
        // 查询条件: 订单编号 明细获取, 明细的状态0
        queryParam.setOrders(orders);
        queryParam.setState(Orderdetail.STATE_NOT_IN);
        // 查询订单下的未入库的明细的个数getCount
        Long count = orderdetailDao.getCount(queryParam, null, null);
        // 4.2 如果还有明细没有入库count > 0
        // 不需要操作
        // 4.3 如果不存在未入库的明细count = 0
        if (count == 0) {
            // 更新订单:
            // 4.3.1 入库日期： 系统时间
            orders.setEndtime(od.getEndtime());
            // 4.3.2 库管员 登陆用户
            orders.setEnder(empuuid);
            // 4.3.3 状态 3: 已入库
            orders.setState(Orders.STATE_END);
        }
    }

    @Override
    @Transactional
    public void doOutStore(Long empuuid, Long storeuuid, Long uuid) {
        // 1. 明细表: 持久态
        Orderdetail od = orderdetailDao.get(uuid);
        // 1.1 不能重复出库
        if(!Orderdetail.STATE_NOT_OUT.equals(od.getState())) {
            throw new ErpException("亲，不能重复出库!");
        }
        // 1.2 结束日期 系统时间
        od.setEndtime(new Date());
        // 1.3 库管员 登陆用户
        od.setEnder(empuuid);
        // 1.4 仓库编号 前端传过来
        od.setStoreuuid(storeuuid);
        // 1.5 状态 1: 已出库
        od.setState(Orderdetail.STATE_OUT);

        // 2. 库存表
        // 条件:
        Storedetail sd = new Storedetail();
        // 商品编号 明细里有
        sd.setGoodsuuid(od.getGoodsuuid());
        // 仓库编号 前端传过来
        sd.setStoreuuid(storeuuid);
        // getList
        List<Storedetail> list = storedetailDao.getList(sd, null, null);
        // 2.1 判断库存是否存在
        long num = -1;
        if(list.size() > 0) {
            // 2.2 如果存在库存信息, 取出库存信息
            sd = list.get(0);
            // 判断数量是否足够 库存数-明细的数量 >= 0
            num = sd.getNum() - od.getNum();
            // 数量不够：报错：库存不足
            if(num < 0) {
                throw new ErpException("库存不足");
            }
            // 数量充足：更新库存数量=库存数-明细的数量
            sd.setNum(num);
        }else {
            // 2.3 不存在库存信息
            // 报错：库存不足
            throw new ErpException("库存不足");
        }

        // 3. 商品库存变更记录
        // 插入记录
        Storeoper log = new Storeoper();
        // 3.1 操作员工编号 登陆用户
        log.setEmpuuid(empuuid);
        // 3.2 操作日期 系统时间, 让入库的时间保持一致
        log.setOpertime(od.getEndtime());
        // 3.3 仓库编号 前端传过来
        log.setStoreuuid(storeuuid);
        // 3.4 商品编号 明细有
        log.setGoodsuuid(od.getGoodsuuid());
        // 3.5 数量 明细的数量
        log.setNum(od.getNum());
        // 3.6 操作的类型 2:出库
        log.setType(Storeoper.TYPE_OUT);
        storeoperDao.add(log);

        // 4. 订单表, 订单进入持久态
        Orders orders = od.getOrders();
        // 4.1 判断该订单下是否所有明细都出库了?
        Orderdetail queryParam = new Orderdetail();
        // 条件: 该订单, 状态:0
        queryParam.setOrders(orders);
        queryParam.setState(Orderdetail.STATE_NOT_OUT);
        // 通过查询明细表中属于该订单的未出库的明细数量 count
        Long count = orderdetailDao.getCount(queryParam, null, null);
        if(count == 0) {
            // 4.3 count = 0
            // 更新：
            // 库管员 登陆用户
            orders.setEnder(empuuid);
            // 出库日期 系统时间
            orders.setEndtime(od.getEndtime());
            // 状态 1: 已出库ub
            orders.setState(Orders.STATE_OUT);
            //Long userid, String toaddress, String addressee, String tele, String info
            // 查询客户信息
            Supplier customer = supplierDao.get(orders.getSupplieruuid());
            // 在线预约下单, 调用的是bos物流，得到运单号
            Long waybillsn = waybillWs.addWaybill(1l, customer.getAddress(), customer.getName(), customer.getTele(), "--");
            // 更新运单号
            orders.setWaybillsn(waybillsn);
        }
        // 4.2 count > 0 不需要
    }

    @Override
    public Map<String, Object> doOutStoreSPC(Long empuuid, Long storeuuid, Long uuid) {
    	return orderdetailDao.doOutStoreSPC(empuuid, storeuuid, uuid);
    }
}
