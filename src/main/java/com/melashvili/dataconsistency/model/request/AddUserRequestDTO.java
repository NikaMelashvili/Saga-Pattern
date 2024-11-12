package com.melashvili.dataconsistency.model.request;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AddUserRequestDTO {
    private String firstName;
    private String lastName;
    private Date birthDate;
    private String address;
}
