package com.nishit.novopay.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nishit.novopay.enums.TransactionStatus;
import com.nishit.novopay.enums.TransactionType;

@Entity
@Table(name = "TRANSACTION")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USERUUID", nullable = false, unique = true, updatable = false)
	private UUID transactionid;
	
	@Column(name = "AMOUNT", precision = 20, scale = 2, nullable = false, updatable = false)
	private BigDecimal amount;
	
	@Column(name = "TRANSACTIONTYPE", nullable = false, updatable = false)
	private TransactionType type;
	
	@Column(name = "TRANSACTIONSTATUS", nullable = false, updatable = false)
	private TransactionStatus status;
	
	@Column(name = "OCCUREDAT", columnDefinition =  "TIMESTAMP", nullable = false, updatable = false)
	private LocalDateTime occuredAt;
	
	@Column(name = "SECONDPARTYWALLETID", nullable = true, updatable = false)
	private UUID secondPartyWalletID;
	
	@Column(name = "CHARGE", precision = 20, scale = 2, nullable = false, updatable = false)
	private BigDecimal charge;
	
	@Column(name = "COMMISSION", precision = 20, scale = 2, nullable = false, updatable = false)
	private BigDecimal commision;
	
	@Column(name = "FINALAMOUNTAFTERCHARGES", precision = 20, scale = 2, nullable = false, updatable = false)
	private BigDecimal finalAmountAfterCharges;
	
	@Column(name = "BALANCEBEFORE", precision = 20, scale = 2, nullable = false, updatable = false)
	private BigDecimal balanceBefore;
	
	@Column(name = "BALANCEAFTER", precision = 20, scale = 2, nullable = false, updatable = false)
	private BigDecimal balanceAfter;

	public UUID getTransactionid() {
		return transactionid;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public TransactionType getType() {
		return type;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public LocalDateTime getOccuredAt() {
		return occuredAt;
	}

	public UUID getSecondPartyWalletID() {
		return secondPartyWalletID;
	}

	public BigDecimal getCharge() {
		return charge;
	}

	public BigDecimal getCommision() {
		return commision;
	}

	public BigDecimal getFinalAmountAfterCharges() {
		return finalAmountAfterCharges;
	}

	public BigDecimal getBalanceBefore() {
		return balanceBefore;
	}

	public BigDecimal getBalanceAfter() {
		return balanceAfter;
	}

	public Transaction setTransactionid(UUID transactionid) {
		this.transactionid = transactionid;
		return this;
	}

	public Transaction setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public Transaction setType(TransactionType type) {
		this.type = type;
		return this;
	}

	public Transaction setStatus(TransactionStatus status) {
		this.status = status;
		return this;
	}

	public Transaction setOccuredAt(LocalDateTime occuredAt) {
		this.occuredAt = occuredAt;
		return this;
	}

	public Transaction setSecondPartyWalletID(UUID secondPartyWalletID) {
		this.secondPartyWalletID = secondPartyWalletID;
		return this;
	}

	public Transaction setCharge(BigDecimal charge) {
		this.charge = charge;
		return this;
	}

	public Transaction setCommision(BigDecimal commision) {
		this.commision = commision;
		return this;
	}

	public Transaction setFinalAmountAfterCharges(BigDecimal finalAmount) {
		this.finalAmountAfterCharges = finalAmount;
		return this;
	}

	public Transaction setBalanceBefore(BigDecimal balanceBefore) {
		this.balanceBefore = balanceBefore;
		return this;
	}

	public Transaction setBalanceAfter(BigDecimal balanceAfter) {
		this.balanceAfter = balanceAfter;
		return this;
	}
	
	
	
	
}
