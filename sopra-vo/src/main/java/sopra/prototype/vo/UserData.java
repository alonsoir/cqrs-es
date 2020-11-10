package sopra.prototype.vo;

import javax.persistence.*;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;

import io.swagger.annotations.ApiModelProperty;

@ManagedResource
@Entity
@Table(name = "USER_DATA")
public class UserData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "The auto-generated id of the user data", required = true)
	@Column(name = "ID_USER_DATA")
	private Integer idUserData;
	@ApiModelProperty(notes = "The name of the user data", required = true)
	@Column(name = "NAME",unique = true)
	private String name;
	@ApiModelProperty(notes = "The date when the user data has been registered", required = true)
	@Column(name = "DATE_REGISTER")
	private String dateRegister;

	public UserData() {
		super();
	}

	public UserData(String name, String dateRegister) {
		super();
		this.name = name;
		this.dateRegister = dateRegister;
	}

	@ManagedAttribute
	public Integer getIdUserData() {
		return idUserData;
	}

	@ManagedAttribute
	public void setIdUserData(Integer idUserData) {
		this.idUserData = idUserData;
	}

	@ManagedAttribute
	public String getName() {
		return name;
	}

	@ManagedAttribute
	public void setName(String name) {
		this.name = name;
	}

	@ManagedAttribute
	public String getDateRegister() {
		return dateRegister;
	}

	@ManagedAttribute
	public void setDateRegister(String dateRegister) {
		this.dateRegister = dateRegister;
	}

}
