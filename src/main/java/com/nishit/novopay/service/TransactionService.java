package com.nishit.novopay.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nishit.novopay.enums.TransactionStatus;
import com.nishit.novopay.enums.TransactionType;
import com.nishit.novopay.exception.InsufficientFundsException;
import com.nishit.novopay.exception.TransactionIdNotFoundException;
import com.nishit.novopay.exception.UserDetailNotFoundException;
import com.nishit.novopay.exception.WalletInvalidException;
import com.nishit.novopay.model.Transaction;
import com.nishit.novopay.model.User;
import com.nishit.novopay.model.Wallet;
import com.nishit.novopay.payload.TransactionStatusPayload;
import com.nishit.novopay.repository.TransactionRepository;
import com.nishit.novopay.repository.UserRepository;
import com.nishit.novopay.repository.WalletRepository;

@Service
@Transactional
public class TransactionService {
	
	private static final BigDecimal charge = new BigDecimal(0.2);
	private static final BigDecimal commission = new BigDecimal(0.05);
	
	private UserRepository userRepository;
	private TransactionRepository transactionRepository;
	private WalletRepository walletRepository;
	
	@Autowired
	public TransactionService(UserRepository userRepository, TransactionRepository transactionRepository, WalletRepository walletRepository) {
		this.userRepository = userRepository;
		this.transactionRepository = transactionRepository;
		this.walletRepository = walletRepository;
	}
	
	public boolean addMoneyToUserWallet(String username, BigDecimal amount) throws UserDetailNotFoundException, WalletInvalidException {
		User user = userRepository.findByUsername(username).orElse(null);
		
		if(user == null) {
			throw new UserDetailNotFoundException("User deatils not found.");
		}
		
		return addMoneyToWallet(user.getWallet(), null, amount);
	}
	
	public boolean transferMoneyToUserWallet(String senderUserName, String recipientUsername, BigDecimal amount) throws UserDetailNotFoundException, WalletInvalidException, InsufficientFundsException {
		User sender = userRepository.findByUsername(senderUserName).orElse(null);
		User recipient = userRepository.findByUsername(recipientUsername).orElse(null);
		
		if(sender == null || recipient == null) {
			throw new UserDetailNotFoundException("User deatils not found.");
		}
		
		return transferMoneyToWallet(sender.getWallet(), recipient.getWallet(), amount);
	}
	
	public TransactionStatusPayload getTransactionStatus(UUID id) throws TransactionIdNotFoundException {
		Transaction transaction = transactionRepository.findById(id).orElse(null);
		if(transaction == null) {
			throw new TransactionIdNotFoundException("Invalid Transaction ID.");
		}
		
		return new TransactionStatusPayload(transaction.getTransactionid(), transaction.getStatus());
	}
	
	
	
	private boolean transferMoneyToWallet(Wallet senderWallet, Wallet recipientWallet, BigDecimal amount) throws WalletInvalidException, InsufficientFundsException {
		if(senderWallet == null || recipientWallet == null) {
			throw new WalletInvalidException("Issue with user's wallet");
		}
		
		deductMoneyFromWallet(senderWallet, recipientWallet, amount);
		
		addMoneyToWallet(recipientWallet, senderWallet, amount);
		
		return true;
	}
	
	
	private boolean addMoneyToWallet(Wallet wallet, Wallet secondPartyWallet, BigDecimal amount) throws WalletInvalidException {
		if(wallet == null) {
			throw new WalletInvalidException("Issue with user's wallet");
		}
		
		BigDecimal finalBalance = wallet.getBalance().add(amount);
		
		Transaction transaction = new Transaction().setAmount(amount)
										.setType(TransactionType.CREDIT)
										.setStatus(TransactionStatus.SUCCESSFUL)
										.setOccuredAt(LocalDateTime.now())
										.setCharge(new BigDecimal(0.0))
										.setCommision(new BigDecimal(0.0))
										.setFinalAmountAfterCharges(amount)
										.setBalanceBefore(wallet.getBalance())
										.setBalanceAfter(finalBalance)
										.setWallet(wallet);
		if(secondPartyWallet != null) {
			transaction.setSecondPartyWalletID(secondPartyWallet.getWalletid());
		}
		
		transactionRepository.save(transaction);
		
		wallet.setBalance(finalBalance);
		
		walletRepository.save(wallet);
		
		return true;								
	}
	
	
	private boolean deductMoneyFromWallet(Wallet wallet, Wallet secondPartyWallet, BigDecimal amount) throws WalletInvalidException, InsufficientFundsException {
		if(wallet == null || secondPartyWallet == null) {
			throw new WalletInvalidException("Issue with user's wallet");
		}
		
		BigDecimal charge = calculateCharge(amount);
		BigDecimal commission = calculateCommission(amount);
		
		BigDecimal finalAmountAfterCharges = amount.add(charge).add(commission);
		
		BigDecimal finalBalance = wallet.getBalance().subtract(finalAmountAfterCharges);
		
		if(finalBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new InsufficientFundsException("Transaction declined due to insufficient funds");
		}
		
		Transaction transaction = new Transaction().setAmount(amount)
										.setType(TransactionType.DEBIT)
										.setStatus(TransactionStatus.SUCCESSFUL)
										.setOccuredAt(LocalDateTime.now())
										.setCharge(charge)
										.setCommision(commission)
										.setFinalAmountAfterCharges(finalAmountAfterCharges)
										.setBalanceBefore(wallet.getBalance())
										.setBalanceAfter(finalBalance)
										.setWallet(wallet)
										.setSecondPartyWalletID(secondPartyWallet.getWalletid());
		
		transactionRepository.save(transaction);
		
		wallet.setBalance(finalBalance);
		
		walletRepository.save(wallet);
		
		return true;								
	}
	
	
	
	private BigDecimal calculateCharge(BigDecimal amount) {
		return amount.multiply(charge).divide(new BigDecimal(100.00));
	}
	
	private BigDecimal calculateCommission(BigDecimal amount) {
		return amount.multiply(commission).divide(new BigDecimal(100.00));
	}
}
