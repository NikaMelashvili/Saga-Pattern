package com.melashvili.dataconsistency.events;

import lombok.Getter;

@Getter
public class UserCreationFailedEvent {
    private final Integer userId;

    public UserCreationFailedEvent(Integer userId) {
        this.userId = userId;
    }
}
