package com.melashvili.dataconsistency.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequestDTO {
    private int oldId;
    private String firstName;
    private String lastName;
    private String address;
}
