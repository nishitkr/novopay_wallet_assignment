package com.nishit.novopay.model;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "WALLET")
public class Wallet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "WALLETID", nullable = false, unique = true, updatable = false)
	private UUID walletid;
	
	@Column(name = "BALANCE", precision = 20, scale = 2)
	private BigDecimal balance;
	
	@OneToOne(mappedBy = "wallet")
	private User user;

	
	
	
	public Wallet() {
		super();
	}

	public Wallet(BigDecimal balance) {
		this.balance = balance;
	}

	public UUID getWalletid() {
		return walletid;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public User getUser() {
		return user;
	}

	public void setWalletid(UUID walletid) {
		this.walletid = walletid;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setUser(User user) {
		this.user = user;
	}

		
	
}
