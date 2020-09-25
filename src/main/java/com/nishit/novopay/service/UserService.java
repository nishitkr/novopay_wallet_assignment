package com.nishit.novopay.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nishit.novopay.model.User;
import com.nishit.novopay.payload.UserPayload;
import com.nishit.novopay.repository.UserRepository;

@Service
public class UserService {
	
	private UserRepository userRepository;
	
	@Autowired
	UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	public void addUser(UserPayload userPayload) {
		User user =   new User().setName(userPayload.getName())
								.setEmail(userPayload.getEmail())
								.setPhone(userPayload.getPhone())
								.setAddress(userPayload.getAddress());
		userRepository.save(user);
	}
}
