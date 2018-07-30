package cn.itcast.erp.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 商品实体类
 */
@Entity
@Table(name="goods")
public class Goods implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(generator="goodsKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="goodsKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters= {@Parameter(name="sequence_name",value="goods_seq")}
            )
    private Long uuid;//编号
    private String name;//名称
    private String origin;//产地
    private String producer;//厂家
    private String unit;//计量单位
    private Double inprice;//进货价格
    private Double outprice;//销售价格
    //private Long goodstypeuuid;//商品类型
    @ManyToOne(targetEntity=Goodstype.class)
    @JoinColumn(name="goodstypeuuid",referencedColumnName="uuid")
    private Goodstype goodstype;

    public Goodstype getGoodstype() {
        return goodstype;
    }
    public void setGoodstype(Goodstype goodstype) {
        this.goodstype = goodstype;
    }
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
    public String getOrigin() {
        return origin;
    }
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    public String getProducer() {
        return producer;
    }
    public void setProducer(String producer) {
        this.producer = producer;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public Double getInprice() {
        return inprice;
    }
    public void setInprice(Double inprice) {
        this.inprice = inprice;
    }
    public Double getOutprice() {
        return outprice;
    }
    public void setOutprice(Double outprice) {
        this.outprice = outprice;
    }

}
