package com.rs.springsecuritydemo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Data
public class ApiException {
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timeStamp;

}
