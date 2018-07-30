package cn.itcast.erp.action;

import javax.annotation.Resource;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import cn.itcast.erp.biz.IInventoryBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Inventory;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("inventoryAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("inventory")
public class InventoryAction extends BaseAction<Inventory> {

    private IInventoryBiz inventoryBiz;

    @Resource(name="inventoryBiz")
    public void setInventoryBiz(IInventoryBiz inventoryBiz) {
        this.inventoryBiz = inventoryBiz;
        super.setBaseBiz(this.inventoryBiz);
    }
    
    
    /**
     * 盘盈盘亏审核
     */
    public void doCheck() {
    	try {
    		
			inventoryBiz.doCheck(WebUtil.getLoginUser().getUuid(),getId());
			WebUtil.ajaxReturn(true, "审核成功");
		} catch (ErpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebUtil.ajaxReturn(false, e.getMessage());
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "审核失败");
		}
    	
    }
    
    @Override
    public void add() {
        try {
            Inventory inventory = getT();
            inventory.setCreater(WebUtil.getLoginUser().getUuid());
            inventoryBiz.add(inventory);
            WebUtil.ajaxReturn(true, "新增成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "新增失败");
        }
    }
}
