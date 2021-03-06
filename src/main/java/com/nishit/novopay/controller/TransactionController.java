package com.nishit.novopay.controller;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Digits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nishit.novopay.exception.InsufficientFundsException;
import com.nishit.novopay.exception.ReversalNotPossible;
import com.nishit.novopay.exception.TransactionIdNotFoundException;
import com.nishit.novopay.exception.UserDetailNotFoundException;
import com.nishit.novopay.exception.WalletInvalidException;
import com.nishit.novopay.payload.AddMoneyPayload;
import com.nishit.novopay.payload.TransactionChargesPayload;
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
				if(username.equals(transferMoneyPayload.getRecipientUsername())) {
					throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED, "Sender and recipient can't be same");
				}
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
	
	@RequestMapping(value = "/reverse", method = RequestMethod.POST)
	public void reverseTransaction(@RequestParam("user") String username, @RequestParam("pwd") String password,
			@RequestParam("id") String transactionid) {
		
		if (!userCredentialService.isAdmin(username, password)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password invalid.");
		}
		
		UUID transactionuuid;
		try {
			transactionuuid = UUID.fromString(transactionid);
		}
		catch(IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Transaction ID.");
		}
		
		try {
			transactionService.reverseTransaction(transactionuuid);
		} catch (TransactionIdNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid Transaction ID.");
		} catch (ReversalNotPossible e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet missing for reversal.");
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
	
	@RequestMapping(value = "/calccharges", method = RequestMethod.GET)
	public TransactionChargesPayload computeCharges(@RequestParam("amount") @Digits(integer =  20, fraction = 2) BigDecimal amount)  {
		return transactionService.calculateTransactionCharges(amount);
	}

}
