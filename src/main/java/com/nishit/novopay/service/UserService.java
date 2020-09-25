package com.nishit.novopay.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nishit.novopay.exception.UserDetailNotFoundException;
import com.nishit.novopay.exception.WalletInvalidException;
import com.nishit.novopay.model.Transaction;
import com.nishit.novopay.model.User;
import com.nishit.novopay.model.UserCredential;
import com.nishit.novopay.model.Wallet;
import com.nishit.novopay.payload.PassbookEntryPayload;
import com.nishit.novopay.payload.UserPayload;
import com.nishit.novopay.repository.UserCredentialRepository;
import com.nishit.novopay.repository.UserRepository;
import com.nishit.novopay.repository.WalletRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;
	private UserCredentialRepository userCredentialRepository;
	private WalletRepository walletRepository;
	
	@Autowired
	UserService(UserRepository userRepository, UserCredentialRepository userCredentialRepository, WalletRepository walletRepository) {
		this.userRepository = userRepository;
		this.userCredentialRepository = userCredentialRepository;
		this.walletRepository = walletRepository;
	}
	
	@Transactional
	public void addUser(UserPayload userPayload) {
		User user =   new User().setUsername(userPayload.getUsername())
								.setName(userPayload.getName())
								.setEmail(userPayload.getEmail())
								.setPhone(userPayload.getPhone())
								.setAddress(userPayload.getAddress())
								.setWallet(new Wallet(new BigDecimal(0.0)));
		
		User savedUser = userRepository.save(user);
		userCredentialRepository.save(new UserCredential(savedUser.getUseruuid(), savedUser.getUsername(), userPayload.getPassword()));
	}
	
	public List<PassbookEntryPayload> getPassbookEntries(String username) throws UserDetailNotFoundException, WalletInvalidException {
		User user = userRepository.findByUsername(username).orElse(null);
		if(user == null) {
			throw new UserDetailNotFoundException("User deatils not found.");
		}
		
		Wallet wallet = walletRepository.findById(user.getWallet().getWalletid()).orElse(null);
		if(wallet == null) {
			throw new WalletInvalidException("Issue with user's wallet");
		}
		
		return convertToPassbookEntries(wallet.getTransactions());
		
	}
	
	private List<PassbookEntryPayload> convertToPassbookEntries(List<Transaction> transactions) {
		List<PassbookEntryPayload> passbookEntries = new ArrayList<PassbookEntryPayload>();
		
		for(Transaction transaction : transactions) {
			
			PassbookEntryPayload passbookEntry = new PassbookEntryPayload().setTransactionid(transaction.getTransactionid())
														.setType(transaction.getType())
														.setStatus(transaction.getStatus())
														.setOccuredAt(transaction.getOccuredAt())
														.setAmount(transaction.getAmount())
														.setCharge(transaction.getCharge())
														.setCommision(transaction.getCommision())
														.setFinalAmountAfterCharges(transaction.getFinalAmountAfterCharges())
														.setBalanceAfter(transaction.getBalanceAfter());
			
			if(transaction.getSecondPartyWalletID() != null) {
				Wallet wallet = walletRepository.findById(transaction.getSecondPartyWalletID()).orElse(null);
				if(wallet != null) {
					passbookEntry.setSecondPartyUsername(wallet.getUser().getUsername());
				}
			}
			
			
			passbookEntries.add(passbookEntry);
		}
		
		return passbookEntries;
	}

}
