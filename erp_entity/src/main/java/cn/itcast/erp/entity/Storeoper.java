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
 * 仓库操作记录实体类
 */
@Entity
@Table(name="storeoper")
public class Storeoper {

    /**
     * 操作类型:1=入库
     */
    public static final String TYPE_IN="1";

    /**
     * 操作类型:2=出库
     */
    public static final String TYPE_OUT="2";

    @Id
    @GeneratedValue(generator="storeoperKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="storeoperKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters= {@Parameter(name="sequence_name",value="storeoper_seq")}
            )
    private Long uuid;//编号
    private Long empuuid;//操作员工编号
    private java.util.Date opertime;//操作日期
    private Long storeuuid;//仓库编号
    private Long goodsuuid;//商品编号
    private Long num;//数量
    private String type;//1：入库 2：出库

    @Transient
    private String storeName;
    @Transient
    private String goodsName;
    @Transient
    private String empName;

    public Long getUuid() {
        return uuid;
    }
    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }
    public Long getEmpuuid() {
        return empuuid;
    }
    public void setEmpuuid(Long empuuid) {
        this.empuuid = empuuid;
    }
    public java.util.Date getOpertime() {
        return opertime;
    }
    public void setOpertime(java.util.Date opertime) {
        this.opertime = opertime;
    }
    public Long getStoreuuid() {
        return storeuuid;
    }
    public void setStoreuuid(Long storeuuid) {
        this.storeuuid = storeuuid;
    }
    public Long getGoodsuuid() {
        return goodsuuid;
    }
    public void setGoodsuuid(Long goodsuuid) {
        this.goodsuuid = goodsuuid;
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
    public String getEmpName() {
        return empName;
    }
    public void setEmpName(String empName) {
        this.empName = empName;
    }

}
