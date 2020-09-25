package com.nishit.novopay.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class UserPayload {
	
	@NotNull
	@Length(min = 1, max = 60)
	private String username;
	
	@NotNull
	@Length(min = 1, max = 100)
	private String name;
	
	private String address;
	
	@Email
	@NotNull
	private String email;
	
	@Pattern(regexp="(^$|[0-9]{10})")
	@NotNull
	private String phone;
	
	@NotNull
	@Length(min = 1, max = 50)
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
}
