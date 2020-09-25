package com.nishit.novopay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nishit.novopay.model.User;
import com.nishit.novopay.model.UserCredential;
import com.nishit.novopay.payload.UserPayload;
import com.nishit.novopay.repository.UserCredentialRepository;
import com.nishit.novopay.repository.UserRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;
	private UserCredentialRepository userCredentialRepository;
	
	@Autowired
	UserService(UserRepository userRepository, UserCredentialRepository userCredentialRepository) {
		this.userRepository = userRepository;
		this.userCredentialRepository = userCredentialRepository;
	}
	
	@Transactional
	public void addUser(UserPayload userPayload) {
		User user =   new User().setUsername(userPayload.getUsername())
								.setName(userPayload.getName())
								.setEmail(userPayload.getEmail())
								.setPhone(userPayload.getPhone())
								.setAddress(userPayload.getAddress());
		
		User savedUser = userRepository.save(user);
		userCredentialRepository.save(new UserCredential(savedUser.getUseruuid(), savedUser.getUsername(), userPayload.getPassword()));
	}
}
