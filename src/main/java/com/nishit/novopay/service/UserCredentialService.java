package com.nishit.novopay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nishit.novopay.repository.UserCredentialRepository;

@Service
public class UserCredentialService {
	
	private UserCredentialRepository userCredentialRepository;
	
	@Autowired
	public UserCredentialService(UserCredentialRepository userCredentialRepository) {
		this.userCredentialRepository = userCredentialRepository;
	}
	
	public boolean isValidCredential(String username, String password) {
		if(userCredentialRepository.findByUsernameAndPassword(username, password).orElse(null) != null) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isAdmin(String username, String password) {
		if(username.equals("admin") && password.equals("admin")) {
			return true;
		}
		else {
			return false;
		}
	}
}
