package com.rs.springsecuritydemo.exception;

import com.rs.springsecuritydemo.user.exception.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
//    @Value(value = "${data.exception.message1}")
//    private String message1;
//    @Value(value = "${data.exception.message2}")
//    private String message2;
//    @Value(value = "${data.exception.message3}")
//    private String message3;

    @ExceptionHandler(value = {UserAlreadyExistsException.class})
    public ResponseEntity<ApiException> handleUserAlreadyExistsException(UserAlreadyExistsException userAlreadyExistsException) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ApiException exception = new ApiException(
                userAlreadyExistsException.getCode(),
                userAlreadyExistsException.getMessage(),
                httpStatus,
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<ApiException>(exception, httpStatus);
    }
}
