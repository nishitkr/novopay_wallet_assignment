package com.nishit.novopay.model;

import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "USER")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USERUUID", nullable = false, unique = true, updatable = false)
	private UUID useruuid;
	
	@Column(name = "USERNAME", nullable = false, unique = true, updatable = false, length = 60)
	private String username;
	
	@Column(name = "NAME", length = 100, nullable = false, updatable = true)
	private String name;
	
	@Email
	@Column(name = "EMAIL", length = 100, nullable = true, updatable = true)
	private String email;
	
	@Pattern(regexp="(^$|[0-9]{10})")
	@Column(name = "PHONE", length = 10, nullable = true, updatable = true)
	private String phone;
	
	@Column(name = "ADDRESS", length = 100, nullable = true, updatable = true)
	private String address;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "walletid", referencedColumnName = "walletid")
	private Wallet wallet;
	

	public UUID getUseruuid() {
		return useruuid;
	}

	public String getUsername() {
		return username;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}
	
	public Wallet getWallet() {
		return wallet;
	}


	public User setUseruuid(UUID useruuid) {
		this.useruuid = useruuid;
		return this;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public User setName(String name) {
		this.name = name;
		return this;
	}

	public User setEmail(String email) {
		this.email = email;
		return this;
	}

	public User setPhone(String phone) {
		this.phone = phone;
		return this;
	}

	public User setAddress(String address) {
		this.address = address;
		return this;
	}
	
	public User setWallet(Wallet wallet) {
		this.wallet = wallet;
		return this;
	}

}
