package com.nishit.novopay.payload;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

public class TransactionChargesPayload {
	
	@Digits(integer =  20, fraction = 2)
	private BigDecimal amount;
	
	@Digits(integer =  20, fraction = 2)
	private BigDecimal charge;
	
	@Digits(integer =  20, fraction = 2)
	private BigDecimal commission;

	public BigDecimal getAmount() {
		return amount;
	}

	public TransactionChargesPayload setAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public BigDecimal getCharge() {
		return charge;
	}

	public TransactionChargesPayload setCharge(BigDecimal charge) {
		this.charge = charge;
		return this;
	}

	public BigDecimal getCommission() {
		return commission;
	}

	public TransactionChargesPayload setCommission(BigDecimal commission) {
		this.commission = commission;
		return this;
	}
	
	
}
