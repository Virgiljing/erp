package cn.itcast.erp.action;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import cn.itcast.erp.biz.IOrdersBiz;
import cn.itcast.erp.entity.Orders;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.redsun.bos.ws.Waybilldetail;
import com.redsun.bos.ws.impl.IWaybillWs;

import cn.itcast.erp.biz.IReturnordersBiz;
import cn.itcast.erp.entity.Emp;
import cn.itcast.erp.entity.Returnorderdetail;
import cn.itcast.erp.entity.Returnorders;
import cn.itcast.erp.exception.ErpException;
import cn.itcast.erp.util.WebUtil;

@Controller("returnordersAction")
@Scope("prototype")
@ParentPackage("struts-default")
@Namespace("/")
@Action("returnorders")
public class ReturnordersAction extends BaseAction<Returnorders> {

    private IReturnordersBiz returnordersBiz;
    @Autowired
    private IOrdersBiz ordersBiz;
    private String json;

    public void setJson(String json) {
        this.json = json;
    }

    @Resource(name = "returnordersBiz")
    public void setReturnordersBiz(IReturnordersBiz returnordersBiz) {
        this.returnordersBiz = returnordersBiz;
        super.setBaseBiz(this.returnordersBiz);
    }
    /**
     * 销售退货审核
     * 
     */
    public void doCheck(){
    	// 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if(null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
    	try {
			returnordersBiz.doCheck(loginUser.getUuid(),getId());
			WebUtil.ajaxReturn(true, "销售退货审核成功！");
		}catch (ErpException e) {
			WebUtil.ajaxReturn(true, e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			WebUtil.ajaxReturn(true, "销售退货审核失败！");
			e.printStackTrace();
		}
    }
    
    /**
     * 采购退货审核
     */
    public void type1outGoodsDoCheck() {
    	Emp loginUser = WebUtil.getLoginUser();
    	if(null == loginUser) {
    		WebUtil.ajaxReturn(false, "请先登录再操作");
    		return;
    	}
    	try {
			returnordersBiz.type1outGoodsDoCheck(loginUser.getUuid(),getId());
			WebUtil.ajaxReturn(true, "审核成功！！");
		} catch (ErpException e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "审核异常");
		}
    }

   
    public void myListByPage() {
        Emp loginUser = WebUtil.getLoginUser();
        if (null != loginUser) {
            // 判断查询条件
            if (null == getT1()) {
                // 构建查询条件
                setT1(new Returnorders());
            }
            // 设置查询条件为登陆用户,根据下单员查询
            getT1().setCreater(loginUser.getUuid());
            super.listByPage();
        }
    }

    /* 
     * 采购退货登记
     */
    public void add() {
        // 判断用户是否登陆
        Emp loginUser = WebUtil.getLoginUser();
        if (null == loginUser) {
            WebUtil.ajaxReturn(false, "你还没有登陆");
            return;
        }
        //System.out.println(getT());
        try {
            // 获取前端提交的订单,主要是得到订单的编号
            Returnorders returnorders = getT();
            Long ordersuuid = returnorders.getOrdersuuid();
            // 获取原订单
            Orders orders = ordersBiz.get(ordersuuid);

            // 把明细的json字符串转成订单明细列表
            List<Returnorderdetail> returnorderdetails = JSON.parseArray(json, Returnorderdetail
                    .class);
            for (int i = 0; i < returnorderdetails.size(); i++) {
                returnorderdetails.get(i).setUuid(null);
                // 原订单 商品数量
                Long num = orders.getOrderDetails().get(i).getNum();
                // 退货数量
                Long returnnum = returnorderdetails.get(i).getReturnnum();
                if (null == returnnum) {
                    returnnum = 0L;
                    returnorderdetails.get(i).setReturnnum(returnnum);
                }
                if (returnnum > num) {
                    WebUtil.ajaxReturn(false, "退货数量不能大于原数量");
                    return;
                }
            }
            //设置供应商
            returnorders.setSupplieruuid(orders.getSupplieruuid());
            // 设置订单下的明细
            returnorders.setReturnorderdetails(returnorderdetails);
//            // 设置下单人
            returnorders.setCreater(loginUser.getUuid());
//            // 设置订单的类型 采购
            returnorders.setType(Returnorders.TYPE_IN);
            // 设置订单状态 未审核
            returnorders.setState(Returnorders.STATE_CREATE);
            // 设置原订单id
            returnorders.setOrdersuuid(Long.valueOf(ordersuuid));
            returnordersBiz.add(returnorders);
            WebUtil.ajaxReturn(true, "添加订单成功");
        } catch (ErpException e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            WebUtil.ajaxReturn(false, "添加订单失败");
        }
    } 
    
    
    /**
     * 销售退单新增(赵萌)
     */
    public void addSaleReturnOrder(){
    	//拿到前端传递来的参数,获取退单对象,拿到客户的编号
	    //将退单明细的对象转换成对象集合,设置退单明细到退单中
	    //拿到登录用户对象	
	    Emp loginUser = WebUtil.getLoginUser();
	    if(loginUser==null){
	    	WebUtil.ajaxReturn(false, "请您先登录");
	    	return;
	    }
	    Returnorders returnorders = getT();
	    try {
		    List<Returnorderdetail> list = JSONArray.parseArray(json, Returnorderdetail.class);
		    returnorders.setReturnorderdetails(list);
		    //设置登记人的id和登记时间及名字
		    returnorders.setCreater(loginUser.getUuid());
		    returnorders.setCreaterName(loginUser.getName());
		    returnorders.setCreatetime(new Date());
	    	//添加销售退单明细
	    	/*for (Returnorderdetail returnorderdetail : list) {
	    		returnorders.getReturnorderdetails().add(returnorderdetail);
			}*/
	    	returnordersBiz.addSaleReturnOrder(returnorders);
	    	WebUtil.ajaxReturn(true, "销售退货订单添加成功");
		} catch (Exception e) {
			e.printStackTrace();
			WebUtil.ajaxReturn(false, "销售退货订单添加失败");
		}
    	
    	
    	
    	
    }
    
    /**
     * 查询物流路径信息
     */
    @Autowired
    private IWaybillWs waybillWs;
    
    private Long waybillsn;
    public void setWaybillsn(Long waybillsn) {
        this.waybillsn = waybillsn;
    }

    public void waybilldetailList() {
        List<Waybilldetail> list = waybillWs.getWaybilldetailList(waybillsn);
        WebUtil.write(list);
    }


    
    
    
}
