package cn.itcast.erp.biz;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.itcast.erp.entity.Supplier;
/**
 * 供应商业务逻辑层接口
 *
 */
public interface ISupplierBiz extends IBaseBiz<Supplier>{

    /**
     * 导出数据
     * @param os
     * @param t1
     * @throws IOException
     */
    void export(OutputStream os,Supplier t1) throws IOException;

    /**
     * 导入数据
     * @param is
     * @throws IOException
     */
    void doImport(InputStream is) throws IOException;
}

