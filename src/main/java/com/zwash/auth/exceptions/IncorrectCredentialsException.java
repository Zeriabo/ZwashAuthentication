package com.zwash.auth.exceptions;

import java.io.Serial;


public class IncorrectCredentialsException extends Exception {

    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -3267837584889499032L;

	public IncorrectCredentialsException(String errorMessage)
	{
		super(errorMessage);
	}
}

