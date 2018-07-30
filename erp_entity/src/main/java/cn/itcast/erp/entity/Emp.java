package cn.itcast.erp.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 员工实体类
 */
@Entity
@Table(name="emp")
public class Emp implements Serializable{

	private static final long serialVersionUID = -7353778127974309870L;
	@Id
    @GeneratedValue(generator="empKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="empKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters= {@Parameter(name="sequence_name",value="emp_seq")}
            )
    private Long uuid;//编号
    private String username;//登陆名
    @JSONField(serialize=false) // 告诉fastJson在把emp对象转成json格式字符串时，忽略这个字段
    @Column(updatable=false) // 通过hibernate持久化器更新数据时忽略这个字段， 但是使用sql语句更新的不受影响
    private String pwd;//登陆密码
    private String name;//真实姓名
    private Long gender;//性别
    private String email;//邮件地址
    private String tele;//联系电话
    private String address;//联系地址
    private java.util.Date birthday;//出生年月日
    //private Long depuuid;//部门编号
    @ManyToOne(targetEntity=Dep.class)
    // foregin key 列名 references 表(列)
    @JoinColumn(name="depuuid")
    private Dep dep;// 员工所在部门
    @Transient
    private String sex;
    

    public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	@ManyToMany(targetEntity=Role.class)
    @JSONField(serialize=false)
    @JoinTable(name="EMP_ROLE",joinColumns= {
            @JoinColumn(name="EMPUUID")
    },inverseJoinColumns= {
            @JoinColumn(name="ROLEUUID")
    })
    private List<Role> roles;// 该用户所拥有的角色

    public Dep getDep() {
        return dep;
    }
    public void setDep(Dep dep) {
        this.dep = dep;
    }
    public Long getUuid() {
        return uuid;
    }
    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getGender() {
        return gender;
    }
    public void setGender(Long gender) {
        this.gender = gender;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getTele() {
        return tele;
    }
    public void setTele(String tele) {
        this.tele = tele;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public java.util.Date getBirthday() {
        return birthday;
    }
    public void setBirthday(java.util.Date birthday) {
        this.birthday = birthday;
    }
    public List<Role> getRoles() {
        return roles;
    }
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

}
