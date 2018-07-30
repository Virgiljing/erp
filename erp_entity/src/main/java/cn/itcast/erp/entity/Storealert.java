package cn.itcast.erp.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 库存数量与待发货数实体
 *
 */
@Entity
@Table(name="view_storealert")
public class Storealert {

    @Id
    private Long uuid; // 商品编号
    private String name; // 商品的名称
    private Long storenum; // 库存数量
    private Long outnum; // 待发货的数量

    public Long getUuid() {
        return uuid;
    }
    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getStorenum() {
        return storenum;
    }
    public void setStorenum(Long storenum) {
        this.storenum = storenum;
    }
    public Long getOutnum() {
        return outnum;
    }
    public void setOutnum(Long outnum) {
        this.outnum = outnum;
    }
}
