package com.zwash.auth.pojos;

import java.io.Serializable;

public class SignInEvent implements Serializable {

    private String username;

    public SignInEvent(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
