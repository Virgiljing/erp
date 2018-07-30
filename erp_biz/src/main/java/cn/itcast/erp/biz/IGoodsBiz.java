package cn.itcast.erp.biz;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.itcast.erp.entity.Goods;
/**
 * 商品业务逻辑层接口
 *
 */
public interface IGoodsBiz extends IBaseBiz<Goods>{
	
	/**
	 * 商品的导入
	 * @param os
	 * @param t1
	 * @throws IOException
	 */
	void exportGoods(OutputStream os,Goods t1) throws IOException;
	 
	/**
	  * 商品的导出
	 * @param is
	 * @throws IOException
	 */
	void doImportGoodsTemplate(InputStream is) throws IOException;
	/**
	 * 商品的导入
	 * @param os
	 * @param t1
	 * @throws IOException
	 */
	void exportGoodsTemplate(OutputStream os,Goods t1) throws IOException;
	
	/**
	 * 商品的导出
	 * @param is
	 * @throws IOException
	 */
	void doImportGoods(InputStream is) throws IOException;
}

