package com.gpate.services;

import com.gpate.dtos.UserDto;

public interface IUserService {
	
	public UserDto validateLogin(String userName);

}
