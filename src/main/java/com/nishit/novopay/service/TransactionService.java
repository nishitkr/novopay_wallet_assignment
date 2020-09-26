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
import com.nishit.novopay.exception.ReversalNotPossible;
import com.nishit.novopay.exception.TransactionIdNotFoundException;
import com.nishit.novopay.exception.UserDetailNotFoundException;
import com.nishit.novopay.exception.WalletInvalidException;
import com.nishit.novopay.model.Transaction;
import com.nishit.novopay.model.User;
import com.nishit.novopay.model.Wallet;
import com.nishit.novopay.payload.TransactionChargesPayload;
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
	
	public boolean reverseTransaction(UUID transactionuuid) throws TransactionIdNotFoundException, ReversalNotPossible {
		Transaction transaction = transactionRepository.findById(transactionuuid).orElse(null);
		if(transaction == null) {
			throw new TransactionIdNotFoundException("Invalid Transaction ID.");
		}
		
		if(transaction.isReversed()) {
			throw new UnsupportedOperationException("Transaction can't be reversed again.");
		}
		
		if(transaction.getType() == TransactionType.DEBIT) {
			revertDebitTransfer(transaction);
		}
		else if(transaction.getType() == TransactionType.CREDIT){
			if(transaction.getSecondPartyWalletID() == null) {
				revertAddMoney(transaction);
			}
			else {
				revertCreditTransfer(transaction);
			}
 		}
		else {
			throw new UnsupportedOperationException("Reversal transactions can't be reversed again.");
		}
		
		transaction.setReversed(true);
		
		transactionRepository.save(transaction);
		
		return true;
	}
	
	
	public TransactionStatusPayload getTransactionStatus(UUID id) throws TransactionIdNotFoundException {
		Transaction transaction = transactionRepository.findById(id).orElse(null);
		if(transaction == null) {
			throw new TransactionIdNotFoundException("Invalid Transaction ID.");
		}
		
		return new TransactionStatusPayload(transaction.getTransactionid(), transaction.getStatus());
	}
	
	
	public TransactionChargesPayload calculateTransactionCharges(BigDecimal amount) {
		return new TransactionChargesPayload().setAmount(amount)
											.setCharge(calculateCharge(amount))
											.setCommission(calculateCommission(amount));
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
	
	
	private boolean revertAddMoney(Transaction transaction) throws ReversalNotPossible {
		Wallet wallet = transaction.getWallet();
		if(wallet == null) {
			throw new ReversalNotPossible("Wallet missing");
		}
		
		BigDecimal amount = transaction.getAmount();
		BigDecimal finalBalance = wallet.getBalance().subtract(amount);
		
		Transaction transactionRecipient = new Transaction().setAmount(amount)
															.setType(TransactionType.REVERSAL)
															.setStatus(TransactionStatus.SUCCESSFUL)
															.setOccuredAt(LocalDateTime.now())
															.setCharge(BigDecimal.ZERO)
															.setCommision(BigDecimal.ZERO)
															.setFinalAmountAfterCharges(amount)
															.setBalanceBefore(wallet.getBalance())
															.setBalanceAfter(finalBalance)
															.setWallet(wallet);
		
		transactionRepository.save(transactionRecipient);
		
		wallet.setBalance(finalBalance);
		
		walletRepository.save(wallet);
		
		return true;
	}
	
	private boolean revertDebitTransfer(Transaction transaction) throws ReversalNotPossible {
		Wallet senderWallet = transaction.getWallet();
		UUID recipientwalletid = transaction.getSecondPartyWalletID();
		
		BigDecimal deductFromRecipientWallet = transaction.getAmount();
		BigDecimal addToSenderWallet = transaction.getFinalAmountAfterCharges();
		
		Wallet recipientWallet = walletRepository.findById(recipientwalletid).orElse(null);
		
		//If recipient has used the money, balance goes to negative
		if(recipientWallet == null) {
			throw new ReversalNotPossible("Wallet missing");
		}
			
		
		BigDecimal recipientFinalBalance = recipientWallet.getBalance().subtract(deductFromRecipientWallet);
		
		Transaction transactionRecipient = new Transaction().setAmount(deductFromRecipientWallet)
														.setType(TransactionType.REVERSAL)
														.setStatus(TransactionStatus.SUCCESSFUL)
														.setOccuredAt(LocalDateTime.now())
														.setCharge(BigDecimal.ZERO)
														.setCommision(BigDecimal.ZERO)
														.setFinalAmountAfterCharges(addToSenderWallet)
														.setBalanceBefore(recipientWallet.getBalance())
														.setBalanceAfter(recipientFinalBalance)
														.setWallet(recipientWallet);
		
		
		
		
		BigDecimal senderFinalBalance = senderWallet.getBalance().add(addToSenderWallet);
		
		Transaction transactionSender = new Transaction().setAmount(addToSenderWallet)
														.setType(TransactionType.REVERSAL)
														.setStatus(TransactionStatus.SUCCESSFUL)
														.setOccuredAt(LocalDateTime.now())
														.setCharge(BigDecimal.ZERO)
														.setCommision(BigDecimal.ZERO)
														.setFinalAmountAfterCharges(addToSenderWallet)
														.setBalanceBefore(senderWallet.getBalance())
														.setBalanceAfter(senderFinalBalance)
														.setWallet(senderWallet);
		
		transactionRepository.save(transactionRecipient);
		
		transactionRepository.save(transactionSender);
		
		recipientWallet.setBalance(recipientFinalBalance);
		
		senderWallet.setBalance(senderFinalBalance);
		
		walletRepository.save(recipientWallet);
		
		walletRepository.save(senderWallet);
		
		return true;
		
	}
	
	private boolean revertCreditTransfer(Transaction transaction) throws ReversalNotPossible {
		Wallet recipientWallet = transaction.getWallet();
		UUID senderwalletid = transaction.getSecondPartyWalletID();
		
		BigDecimal amount = transaction.getAmount();
		BigDecimal deductFromRecipientWallet = amount;
		BigDecimal addToSenderWallet = amount.add(calculateCharge(amount)).add(calculateCommission(amount));
		
		Wallet senderWallet = walletRepository.findById(senderwalletid).orElse(null);
		
		if(senderWallet == null) {
			throw new ReversalNotPossible("Wallet missing");
		}
		
		
		BigDecimal recipientFinalBalance = recipientWallet.getBalance().subtract(deductFromRecipientWallet);
		
		Transaction transactionRecipient = new Transaction().setAmount(deductFromRecipientWallet)
														.setType(TransactionType.REVERSAL)
														.setStatus(TransactionStatus.SUCCESSFUL)
														.setOccuredAt(LocalDateTime.now())
														.setCharge(BigDecimal.ZERO)
														.setCommision(BigDecimal.ZERO)
														.setFinalAmountAfterCharges(amount)
														.setBalanceBefore(recipientWallet.getBalance())
														.setBalanceAfter(recipientFinalBalance)
														.setWallet(recipientWallet);
		
		BigDecimal senderFinalBalance = senderWallet.getBalance().add(addToSenderWallet);
		
		Transaction transactionSender = new Transaction().setAmount(addToSenderWallet)
														.setType(TransactionType.REVERSAL)
														.setStatus(TransactionStatus.SUCCESSFUL)
														.setOccuredAt(LocalDateTime.now())
														.setCharge(BigDecimal.ZERO)
														.setCommision(BigDecimal.ZERO)
														.setFinalAmountAfterCharges(addToSenderWallet)
														.setBalanceBefore(senderWallet.getBalance())
														.setBalanceAfter(senderFinalBalance)
														.setWallet(senderWallet);
		
		transactionRepository.save(transactionRecipient);
		
		transactionRepository.save(transactionSender);
		
		recipientWallet.setBalance(recipientFinalBalance);
		
		senderWallet.setBalance(senderFinalBalance);
		
		walletRepository.save(recipientWallet);
		
		walletRepository.save(senderWallet);
		
		return true;
	}
	
	private BigDecimal calculateCharge(BigDecimal amount) {
		return amount.multiply(charge).divide(BigDecimal.valueOf(100.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	private BigDecimal calculateCommission(BigDecimal amount) {
		return amount.multiply(commission).divide(BigDecimal.valueOf(100.00)).setScale(2, BigDecimal.ROUND_HALF_UP);
	}
}
