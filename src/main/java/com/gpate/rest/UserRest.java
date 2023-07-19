package com.gpate.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gpate.dtos.UserDto;
import com.gpate.model.User;
import com.gpate.services.impl.UserService;

@RestController
@RequestMapping("api")
@CrossOrigin("*")
public class UserRest {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User userData) {
		UserDto user = userService.validateLogin(userData.getUserName());
		
		System.out.println(userData);
		
		if (user.getPassword()!= null && user.getPassword().equals(userData.getPassword())) {
			return ResponseEntity.ok(user);
		}
		return (ResponseEntity<?>) ResponseEntity.internalServerError();
	}

}
