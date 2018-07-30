package cn.itcast.erp.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 角色实体类
 */
@Entity
@Table(name="role")
public class Role {
    @Id
    @GeneratedValue(generator="roleKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="roleKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters= {@Parameter(name="sequence_name",value="role_seq")}
            )
    private Long uuid;//编号
    private String name;//名称

    @ManyToMany(targetEntity=Menu.class)
    // joinColumns 配置是当前实体类在中间表的映射, name的值是中间表中的字段
    // inverseJoinColumns 配置的是另一方在中间表的映射,name的值是中间表中的字段
    @JoinTable(name="ROLE_MENU",joinColumns= {
            @JoinColumn(name="ROLEUUID",referencedColumnName="uuid")
    },inverseJoinColumns= {
            @JoinColumn(name="MENUUUID",referencedColumnName="menuid")
    })
    @JSONField(serialize=false)
    private List<Menu> menus;// 这个角色所拥有的权限

    @ManyToMany(mappedBy="roles")
    @JSONField(serialize=false)
    private List<Emp> emps; // 拥有这个角色所有的用户;

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
    public List<Menu> getMenus() {
        return menus;
    }
    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
    public List<Emp> getEmps() {
        return emps;
    }
    public void setEmps(List<Emp> emps) {
        this.emps = emps;
    }

}
