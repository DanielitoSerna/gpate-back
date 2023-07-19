package com.gpate.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gpate.dtos.UserDto;
import com.gpate.model.User;
import com.gpate.repository.UserRepository;
import com.gpate.services.IUserService;

@Service
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDto validateLogin(String userName) {
		User user = userRepository.findByUserName(userName);
		UserDto userDto = new UserDto();
		if (user != null) {
			userDto = converToDto(user);
		}
		return userDto;
	}
	
	private User converToModel (UserDto userDto) {
		ModelMapper modelMapper = new ModelMapper();
		User user = modelMapper.map(userDto, User.class);
		return user;
	}
	
	private UserDto converToDto (User user) {
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(user, UserDto.class);
		return userDto;
	}

}
