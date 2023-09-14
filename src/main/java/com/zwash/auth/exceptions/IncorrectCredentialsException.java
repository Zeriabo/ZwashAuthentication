package com.zwash.auth.exceptions;


public class IncorrectCredentialsException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -3267837584889499032L;

	public IncorrectCredentialsException(String errorMessage)
	{
		super(errorMessage);
	}
}

