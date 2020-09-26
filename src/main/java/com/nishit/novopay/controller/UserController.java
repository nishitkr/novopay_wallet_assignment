package com.nishit.novopay.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.nishit.novopay.exception.UserDetailNotFoundException;
import com.nishit.novopay.exception.WalletInvalidException;
import com.nishit.novopay.payload.PassbookEntryPayload;
import com.nishit.novopay.payload.UserPayload;
import com.nishit.novopay.service.UserCredentialService;
import com.nishit.novopay.service.UserService;


@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {
	public static final String BASE_URL = "/novopay/api/v1/user";
	
	private UserService userService;
	private UserCredentialService userCredentialService;
	
	@Autowired
	public UserController(UserService userService, UserCredentialService userCredentialService) {
		this.userService = userService;
		this.userCredentialService = userCredentialService;
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void adduser(@Valid @RequestBody UserPayload userPayload) {
		userService.addUser(userPayload);
	}
	
	
	@RequestMapping(value = "/passbook", method = RequestMethod.GET)
	public List<PassbookEntryPayload> viewPassbook(@RequestParam("user") String username, @RequestParam("pwd") String password) {
		if (userCredentialService.isValidCredential(username, password)) {
			try {
				return userService.getPassbookEntries(username);
			} catch (UserDetailNotFoundException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found", e);
			} catch (WalletInvalidException e) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wallet not found", e);
			}
		}
		else {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password invalid.");
		}
	}
	
}
