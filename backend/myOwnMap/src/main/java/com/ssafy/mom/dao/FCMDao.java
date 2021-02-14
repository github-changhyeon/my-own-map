package com.ssafy.mom.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ssafy.mom.model.FCMRegister;
import com.ssafy.mom.model.UserDto;


public interface FCMDao extends JpaRepository<FCMRegister, Integer> {

	String findTokenByUserDto(UserDto userDto);

	int findFcmNoByUserDto(UserDto userDto);

}
