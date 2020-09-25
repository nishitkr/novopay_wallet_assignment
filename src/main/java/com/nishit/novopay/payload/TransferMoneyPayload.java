package com.nishit.novopay.payload;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class TransferMoneyPayload {
	
	@NotNull
	@Length(min = 1, max = 60)
	private String recipientUsername;
	
	@Digits(integer =  20, fraction = 2)
	@DecimalMin("100.00")
	private BigDecimal amount;

	public String getRecipientUsername() {
		return recipientUsername;
	}

	public void setRecipientUsername(String recipientUsername) {
		this.recipientUsername = recipientUsername;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	
}
