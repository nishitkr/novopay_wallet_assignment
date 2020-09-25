package com.nishit.novopay.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USERCREDENTIAL")
public class UserCredential {
	
	@Id
	@Column(name = "USERUUID", nullable = false, unique = true, updatable = false)
	private UUID useruuid;
	
	@Column(name = "USERNAME", nullable = false, unique = true, updatable = false, length = 60)
	private String username;
	
	@Column(name = "PASSWORD", length = 50, nullable = true, updatable = true)
	private String password;
	
	

	public UserCredential() {
		super();
	}

	public UserCredential(UUID useruuid, String username, String password) {
		this.useruuid = useruuid;
		this.username = username;
		this.password = password;
	}

	public UUID getUseruuid() {
		return useruuid;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

}
