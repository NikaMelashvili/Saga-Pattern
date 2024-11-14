package com.melashvili.dataconsistency.events;

import lombok.Getter;

@Getter
public class UserInfoCreationFailedEvent {
    private final Integer userInfoId;

    public UserInfoCreationFailedEvent(Integer userInfoId) {
        this.userInfoId = userInfoId;
    }
}
