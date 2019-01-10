package com.interest.controller.login;

public class LoginFailureException extends RuntimeException {

	private static final long serialVersionUID = 1381277361046202535L;

	public LoginFailureException(String message) {
		super(message);
	}
	
}
