package com.nishit.novopay.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.nishit.novopay.enums.TransactionStatus;
import com.nishit.novopay.enums.TransactionType;

public class PassbookEntryPayload {
	
	private UUID transactionid;
	
	private TransactionType type;
	
	private TransactionStatus status;
	
	private LocalDateTime occuredAt;
	
	private String secondPartyUsername;
	
	private BigDecimal amount;
	
	private BigDecimal charge;
	
	private BigDecimal commision;

	private BigDecimal finalAmountAfterCharges;
	
	private BigDecimal balanceAfter;

	public UUID getTransactionid() {
		return transactionid;
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

	public String getSecondPartyUsername() {
		return secondPartyUsername;
	}

	public BigDecimal getAmount() {
		return amount;
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

	public BigDecimal getBalanceAfter() {
		return balanceAfter;
	}

	public PassbookEntryPayload setTransactionid(UUID transactionid) {
		this.transactionid = transactionid;
		return this;
	}

	public PassbookEntryPayload setType(TransactionType type) {
		this.type = type;
		return this;
	}

	public PassbookEntryPayload setStatus(TransactionStatus status) {
		this.status = status;
		return this;
	}

	public PassbookEntryPayload setOccuredAt(LocalDateTime occuredAt) {
		this.occuredAt = occuredAt;
		return this;
	}

	public PassbookEntryPayload setSecondPartyUsername(String secondPartyUsername) {
		this.secondPartyUsername = secondPartyUsername;
		return this;
	}

	public PassbookEntryPayload setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public PassbookEntryPayload setCharge(BigDecimal charge) {
		this.charge = charge;
		return this;
	}

	public PassbookEntryPayload setCommision(BigDecimal commision) {
		this.commision = commision;
		return this;
	}

	public PassbookEntryPayload setFinalAmountAfterCharges(BigDecimal finalAmountAfterCharges) {
		this.finalAmountAfterCharges = finalAmountAfterCharges;
		return this;
	}

	public PassbookEntryPayload setBalanceAfter(BigDecimal balanceAfter) {
		this.balanceAfter = balanceAfter;
		return this;
	}
	
	
	
}
