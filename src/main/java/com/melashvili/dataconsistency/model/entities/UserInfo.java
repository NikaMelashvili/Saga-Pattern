package com.melashvili.dataconsistency.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "user_info")
@Getter
@Setter
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "address")
    private String address;

    @Transient
    private String sagaId;
}
