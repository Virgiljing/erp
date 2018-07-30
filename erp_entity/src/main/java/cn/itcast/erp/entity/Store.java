package cn.itcast.erp.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * 仓库实体类
 */
@Entity
@Table(name="store")
public class Store {
    @Id
    @GeneratedValue(generator="storeKeyGenerator",strategy=GenerationType.SEQUENCE)
    @GenericGenerator(name="storeKeyGenerator",strategy="org.hibernate.id.enhanced.SequenceStyleGenerator",
                parameters= {@Parameter(name="sequence_name",value="store_seq")}
            )
    private Long uuid;//编号
	private String name;//名称
	private Long empuuid;//员工编号

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
	public Long getEmpuuid() {		
		return empuuid;
	}
	public void setEmpuuid(Long empuuid) {
		this.empuuid = empuuid;
	}

}
