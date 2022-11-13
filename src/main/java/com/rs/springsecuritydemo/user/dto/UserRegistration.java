package com.rs.springsecuritydemo.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class UserRegistration {
    private final String firstName;
    private final String lastName;
    private final String password;
    private final String email;

}
