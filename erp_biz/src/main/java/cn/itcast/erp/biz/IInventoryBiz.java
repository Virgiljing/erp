package cn.itcast.erp.biz;
import cn.itcast.erp.entity.Inventory;
/**
 * 盘盈盘亏业务逻辑层接口
 *
 */
public interface IInventoryBiz extends IBaseBiz<Inventory>{

	/**
	 *  盘盈盘亏审核
	 * @param uuid
	 * @param id
	 */
	void doCheck(Long uuid, Long id);

}

