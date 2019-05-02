package com.bridgelabz.fundoo.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import com.bridgelabz.fundoo.dto.LoginDTO;
import com.bridgelabz.fundoo.dto.UserDTO;
import com.bridgelabz.fundoo.entity.User;
import com.bridgelabz.fundoo.exception.UserException;
import com.bridgelabz.fundoo.response.Response;
import com.bridgelabz.fundoo.response.ResponseToken;

public interface UserService {
	Response onRegister(UserDTO userDto) throws UserException, UnsupportedEncodingException;

	ResponseToken onLogin(LoginDTO loginDto) throws UserException, UnsupportedEncodingException;

	/**
	 * to verify valid emailId
	 */
	Response validateEmailId(String token) throws UserException;

	/**
	 * to send forget password link
	 */
	Response forgetPassword(String emailId) throws UserException, UnsupportedEncodingException;

	/**
	 * use to reset already register user password
	 * 
	 */
	public Response resetPaswords(String token, String password) throws UserException;

	ResponseToken authentication(Optional<User> user, String password)
			throws UnsupportedEncodingException, UserException;

}