package cn.itcast.erp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 订单实体类
 */
@Entity
@Table(name="orders")
public class Orders {

    /**
     * 订单类型：采购
     */
    public static final String TYPE_IN = "1";

    /**
     * 订单类型：销售
     */
    public static final String TYPE_OUT = "2";

    /**
     * 采购订单状态：未审核
     */
    public static final String STATE_CREATE = "0";

    /**
     * 采购订单状态：已审核
     */
    public static final String STATE_CHECK = "1";

    /**
     * 采购订单状态：已确认
     */
    public static final String STATE_START = "2";

    /**
     * 采购订单状态：已入库
     */
    public static final String STATE_END = "3";
    /**
     * 销售订单状态：未出库
     */
    public static final String STATE_NOT_OUT = "0";

    /**
     * 销售订单状态：已出库
     */
    public static final String STATE_OUT = "1";

    @Id
    @GeneratedValue(generator="ordersKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="ordersKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters= {@Parameter(name="sequence_name",value="orders_seq")}
            )
    private Long uuid;//编号
    private java.util.Date createtime;//生成日期
    private java.util.Date checktime;//审核日期
    private java.util.Date starttime;//确认日期
    private java.util.Date endtime;//入库或出库日期
    private String type;//1:采购 2:销售
    private Long creater;//下单员
    private Long checker;//审核员
    private Long starter;//采购员
    private Long ender;//库管员
    private Long supplieruuid;//供应商或客户
    private Double totalmoney;//合计金额
    private String state;//采购: 0:未审核 1:已审核, 2:已确认, 3:已入库；销售：0:未出库 1:已出库
    private Long waybillsn;//运单号
    // CascadeType.PERSIST 相当于save
    @OneToMany(targetEntity=Orderdetail.class,cascade= {CascadeType.ALL},mappedBy="orders")
    private List<Orderdetail> orderDetails;// 订单下的所有明细

    @Transient // @Transient 持久化时忽略这个字段
    private String createrName; // 下单员名称
    @Transient
    private String checkerName;// 审核员名称
    @Transient
    private String starterName;// 确认人名称
    @Transient
    private String enderName;// 库管员名称
    @Transient
    private String supplierName;// 供应商名称

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
    public String getStarterName() {
        return starterName;
    }
    public void setStarterName(String starterName) {
        this.starterName = starterName;
    }
    public String getEnderName() {
        return enderName;
    }
    public void setEnderName(String enderName) {
        this.enderName = enderName;
    }
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<Orderdetail> getOrderDetails() {
        return orderDetails;
    }
    public void setOrderDetails(List<Orderdetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
    public Long getUuid() {
        return uuid;
    }
    public void setUuid(Long uuid) {
        this.uuid = uuid;
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
    public java.util.Date getStarttime() {
        return starttime;
    }
    public void setStarttime(java.util.Date starttime) {
        this.starttime = starttime;
    }
    public java.util.Date getEndtime() {
        return endtime;
    }
    public void setEndtime(java.util.Date endtime) {
        this.endtime = endtime;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
    public Long getStarter() {
        return starter;
    }
    public void setStarter(Long starter) {
        this.starter = starter;
    }
    public Long getEnder() {
        return ender;
    }
    public void setEnder(Long ender) {
        this.ender = ender;
    }
    public Long getSupplieruuid() {
        return supplieruuid;
    }
    public void setSupplieruuid(Long supplieruuid) {
        this.supplieruuid = supplieruuid;
    }
    public Double getTotalmoney() {
        return totalmoney;
    }
    public void setTotalmoney(Double totalmoney) {
        this.totalmoney = totalmoney;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public Long getWaybillsn() {
        return waybillsn;
    }
    public void setWaybillsn(Long waybillsn) {
        this.waybillsn = waybillsn;
    }

}
