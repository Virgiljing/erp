package cn.itcast.erp.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 商品分类实体类
 */
@Entity
@Table(name="goodstype")
public class Goodstype implements Serializable{
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(generator="goodstypeKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="goodstypeKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters= {@Parameter(name="sequence_name",value="goodstype_seq")}
            )
    private Long uuid;//商品类型编号
    private String name;//商品类型名称
    @JSONField(serialize=false)
    @OneToMany(mappedBy="goodstype")
    private List<Goods> goods;

    public List<Goods> getGoods() {
        return goods;
    }
    public void setGoods(List<Goods> goods) {
        this.goods = goods;
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

}
