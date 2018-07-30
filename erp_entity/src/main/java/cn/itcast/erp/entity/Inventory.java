package cn.itcast.erp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 盘盈盘亏实体类
 */
@Entity
@Table(name="inventory")
public class Inventory {
	
	 /**
     * 盘盈盘亏状态：未审核
     */
    public static final String STATE_CREATE = "0";

    /**
     * 盘盈盘亏状态：已审核
     */
    public static final String STATE_CHECK = "1";
	
	/**
	 * 订单类型：盘盈
	 */
	public static final String TYPE_PROFIT = "1";
	
	/**
	 * 订单类型：盘亏
	 */
	public static final String TYPE_LOSS = "2";
	
    @Id
    @GeneratedValue(generator="inventoryKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="inventoryKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
                parameters= {@Parameter(name="sequence_name",value="inventory_seq")}
            )
    private Long uuid;//编号
	private Long goodsuuid;//商品
	private Long storeuuid;//仓库
	private Long num;//数量
	private String type;//类型
	private java.util.Date createtime;//登记日期
	private java.util.Date checktime;//审核日期
	private Long creater;//登记人
	private Long checker;//审核人
	private String state;//状态
	private String remark;//备注
	//为了避免多次向数据库查询,实体类自带empname,商品名,仓库名
	@Transient
	private String createrName;
	@Transient
	private String checkerName;
	@Transient
	private String storeName;
	@Transient
	private String goodsName;
	
	public String getCreaterName() {
		return createrName;
	}
	public void setCreaterName(String createrName) {
		this.createrName = createrName;
	}
	public String getCheckerName() {
		return checkerName;
	}
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}
	
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public Long getUuid() {		
		return uuid;
	}
	public void setUuid(Long uuid) {
		this.uuid = uuid;
	}
	public Long getGoodsuuid() {		
		return goodsuuid;
	}
	public void setGoodsuuid(Long goodsuuid) {
		this.goodsuuid = goodsuuid;
	}
	public Long getStoreuuid() {		
		return storeuuid;
	}
	public void setStoreuuid(Long storeuuid) {
		this.storeuuid = storeuuid;
	}
	public Long getNum() {		
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public String getType() {		
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public java.util.Date getCreatetime() {		
		return createtime;
	}
	public void setCreatetime(java.util.Date createtime) {
		this.createtime = createtime;
	}
	public java.util.Date getChecktime() {		
		return checktime;
	}
	public void setChecktime(java.util.Date checktime) {
		this.checktime = checktime;
	}
	public Long getCreater() {		
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public Long getChecker() {		
		return checker;
	}
	public void setChecker(Long checker) {
		this.checker = checker;
	}
	public String getState() {		
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getRemark() {		
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
