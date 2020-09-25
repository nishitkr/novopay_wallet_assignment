package com.nishit.novopay.payload;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

public class AddMoneyPayload {
	
	//The minimum amount for transaction is set to 10
	//This is done to avoid 0 transaction charges
	//For E.g. : For Rs 10 -> commission will be 0.005 => 0.00
	//And hence users can do multiple transactions of amount less than 100 to avoid charges.
	
	@Digits(integer =  20, fraction = 2)
	@DecimalMin("100.00")
	private BigDecimal amount;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	
	
}
