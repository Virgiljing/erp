package cn.itcast.erp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 供应商实体类
 */
@Entity
@Table(name="supplier")
public class Supplier {

    /**
     * 类型为供应商
     */
    public static final String TYPE_SUPPLIER = "1";

    /**
     * 类型为客户
     */
    public static final String TYPE_CUSTOMER = "2";

    @Id
    @GeneratedValue(generator="supplierKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="supplierKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
    parameters= {@Parameter(name="sequence_name",value="supplier_seq")}
            )
    private Long uuid;//编号
    private String name;//名称
    private String address;//联系地址
    private String contact;//联系人
    private String tele;//联系电话
    private String email;//邮件地址
    private String type;//1：供应商 2：客户

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
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getTele() {
        return tele;
    }
    public void setTele(String tele) {
        this.tele = tele;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
