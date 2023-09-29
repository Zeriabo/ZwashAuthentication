package com.zwash.auth.pojos;

import java.io.Serializable;

public class RegistrationEvent implements Serializable {

    private long userId;

    public RegistrationEvent(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
