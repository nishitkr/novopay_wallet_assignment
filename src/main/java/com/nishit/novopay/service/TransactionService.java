package com.nishit.novopay.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nishit.novopay.enums.TransactionStatus;
import com.nishit.novopay.enums.TransactionType;
import com.nishit.novopay.exception.UserDetailNotFoundException;
import com.nishit.novopay.exception.WalletInvalidException;
import com.nishit.novopay.model.Transaction;
import com.nishit.novopay.model.User;
import com.nishit.novopay.model.Wallet;
import com.nishit.novopay.repository.TransactionRepository;
import com.nishit.novopay.repository.UserRepository;
import com.nishit.novopay.repository.WalletRepository;

@Service
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
		
		return addMoneyToWallet(user.getWallet(), amount);
	}
	
	@Transactional
	public boolean addMoneyToWallet(Wallet wallet, BigDecimal amount) throws WalletInvalidException {
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
										.setBalanceAfter(finalBalance);
		
		transactionRepository.save(transaction);
		
		wallet.setBalance(finalBalance);
		
		walletRepository.save(wallet);
		
		return true;								
	}
	
	private BigDecimal calculateCharge(BigDecimal amount) {
		return amount.multiply(charge);
	}
	
	private BigDecimal calculateCommission(BigDecimal amount) {
		return amount.multiply(commission);
	}
}
