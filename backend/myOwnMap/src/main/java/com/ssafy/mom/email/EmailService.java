package com.ssafy.mom.email;


import org.springframework.stereotype.Service;

import com.ssafy.mom.model.UserDto;

import javassist.NotFoundException;

public interface EmailService {

	public void sendMail(String to,String sub, String text);
	public void sendVerificationMail(UserDto user) throws NotFoundException;
	public void verifyEmail(String key) throws NotFoundException;
}
