package com.rs.springsecuritydemo.user.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
public class UserAlreadyExistsException extends RuntimeException{
    private final String code = "USR-0001";
    public UserAlreadyExistsException(String message){
        super(message);
    }

}
