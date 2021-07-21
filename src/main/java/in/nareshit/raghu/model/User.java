package in.nareshit.raghu.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="usertab")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="uid")
	private Integer id;
	
	@Column(name="uname")
	private String name;
	
	@Column(name="usrname")
	private String username;
	
	@Column(name="upwd")
	private String password;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name="urs_rolestab",
		joinColumns = @JoinColumn(name="uid")
	)
	@Column(name="role")
	private List<String> roles;
}
