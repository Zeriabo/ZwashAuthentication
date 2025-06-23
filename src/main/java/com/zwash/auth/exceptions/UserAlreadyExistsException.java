package com.zwash.auth.exceptions;

import java.io.Serial;

public class UserAlreadyExistsException extends Exception {

    @Serial
    private static final long serialVersionUID = -3267837584889499039L;
    public UserAlreadyExistsException()
    {

    }
	public UserAlreadyExistsException(String  username)
	{
		super(" User "+username +" already exists in the system!");
	}
}
