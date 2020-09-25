package com.nishit.novopay.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.nishit.novopay.payload.UserPayload;
import com.nishit.novopay.service.UserService;


@RestController
@RequestMapping(UserController.BASE_URL)
public class UserController {
	public static final String BASE_URL = "/novopay/api/v1/user";
	
	private UserService userService;
	
	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public void adduser(@Valid @RequestBody UserPayload userPayload) {
		userService.addUser(userPayload);
	}
}
