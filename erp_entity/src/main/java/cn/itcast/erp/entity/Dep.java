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
 * 部门实体类
 */
@Entity
@Table(name="dep")
public class Dep implements Serializable {
  
	private static final long serialVersionUID = -6566881838722591571L;
	@Id
    @GeneratedValue(generator="depKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="depKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters= {@Parameter(name="sequence_name",value="dep_seq")}
            )
    private Long uuid;//编号
    private String name;//部门名称
    private String tele;//联系电话
    // mappedBy 主外键的关系由谁来维护，由多的一方, 指向多方中一方的属性名
    @OneToMany(mappedBy="dep",targetEntity=Emp.class)
    @JSONField(serialize=false)
    private List<Emp> emps;

    public List<Emp> getEmps() {
        return emps;
    }
    public void setEmps(List<Emp> emps) {
        this.emps = emps;
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
    public String getTele() {
        return tele;
    }
    public void setTele(String tele) {
        this.tele = tele;
    }

}
