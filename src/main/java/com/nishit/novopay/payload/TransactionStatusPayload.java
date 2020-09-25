package com.nishit.novopay.payload;

import java.util.UUID;

import com.nishit.novopay.enums.TransactionStatus;

public class TransactionStatusPayload {
	
	private UUID transactionid;
	
	private TransactionStatus status;

		
	
	public TransactionStatusPayload(UUID transactionid, TransactionStatus status) {
		super();
		this.transactionid = transactionid;
		this.status = status;
	}

	public UUID getTransactionid() {
		return transactionid;
	}

	public void setTransactionid(UUID transactionid) {
		this.transactionid = transactionid;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	
	
	
}
