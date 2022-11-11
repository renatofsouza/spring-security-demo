package com.rs.springsecuritydemo.email;

public interface EmailSender {
    void send(String to, String email);
}
