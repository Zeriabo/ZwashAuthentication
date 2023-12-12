package com.zwash.auth.exceptions;


public class IncorrectTokenException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = -3267837584889499032L;

	public IncorrectTokenException(String errorMessage)
	{
		super(errorMessage);
	}
}

