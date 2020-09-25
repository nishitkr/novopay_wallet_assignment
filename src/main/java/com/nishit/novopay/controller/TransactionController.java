package com.nishit.novopay.controller;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nishit.novopay.exception.InsufficientFundsException;
import com.nishit.novopay.exception.TransactionIdNotFoundException;
import com.nishit.novopay.exception.UserDetailNotFoundException;
import com.nishit.novopay.exception.WalletInvalidException;
import com.nishit.novopay.payload.AddMoneyPayload;
import com.nishit.novopay.payload.TransactionStatusPayload;
import com.nishit.novopay.payload.TransferMoneyPayload;
import com.nishit.novopay.service.TransactionService;
import com.nishit.novopay.service.UserCredentialService;

@RestController
@RequestMapping(TransactionController.BASE_URL)
public class TransactionController {

	public static final String BASE_URL = "/novopay/api/v1/transact";

	private UserCredentialService userCredentialService;
	private TransactionService transactionService;

	@Autowired
	public TransactionController(UserCredentialService userCredentialService, TransactionService transactionService) {
		this.userCredentialService = userCredentialService;
		this.transactionService = transactionService;
	}

	// TODO create response payload

	@RequestMapping(value = "/addmoney", method = RequestMethod.POST)
	public void addMoneyToWallet(@RequestParam("user") String username, @RequestParam("pwd") String password,
			@Valid @RequestBody AddMoneyPayload addMoneyPayload) {
		if (userCredentialService.isValidCredential(username, password)) {
			try {
				transactionService.addMoneyToUserWallet(username, addMoneyPayload.getAmount());
			} catch (UserDetailNotFoundException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
			} catch (WalletInvalidException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found", e);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password invalid.");
		}
	}

	@RequestMapping(value = "/transfer", method = RequestMethod.POST)
	public void addMoneyToWallet(@RequestParam("user") String username, @RequestParam("pwd") String password,
			@Valid @RequestBody TransferMoneyPayload transferMoneyPayload) {
		if (userCredentialService.isValidCredential(username, password)) {
			try {
				transactionService.transferMoneyToUserWallet(username, transferMoneyPayload.getRecipientUsername(), transferMoneyPayload.getAmount());
			} catch (UserDetailNotFoundException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
			} catch (WalletInvalidException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found", e);
			} catch (InsufficientFundsException e) {
				throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Transaction declined. Insufficient funds.", e);
			}
		} else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password invalid.");
		}
	}
	
	@RequestMapping(value = "/statusinquiry", method = RequestMethod.GET)
	public TransactionStatusPayload statusInquiry(@RequestParam("id") String id) {
		UUID transactionid;
		try {
			transactionid = UUID.fromString(id);
		}
		catch(IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Transaction ID.");
		}
		
		try {
			return transactionService.getTransactionStatus(transactionid);
		} catch (TransactionIdNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Transaction not found", e);
		}
	}

}
