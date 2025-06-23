package com.zwash.auth.exceptions;

import java.io.Serial;

public class UserIsNotActiveException extends Exception {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = -3267837584889499032L;
    public UserIsNotActiveException()
    {

    }
	public UserIsNotActiveException(String username)
	{
		super(username+ "is not active");
	}
}
