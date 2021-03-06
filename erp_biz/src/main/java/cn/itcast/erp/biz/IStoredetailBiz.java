package cn.itcast.erp.biz;
import java.util.List;

import javax.mail.MessagingException;

import cn.itcast.erp.entity.Storealert;
import cn.itcast.erp.entity.Storedetail;
/**
 * 仓库库存业务逻辑层接口
 *
 */
public interface IStoredetailBiz extends IBaseBiz<Storedetail>{

    /**
     * 获取库存预警列表
     * @return
     */
    List<Storealert> getStorealertList();

    /**
     * 发送预警邮件
     */
    void sendStorealertMail() throws MessagingException;
}

