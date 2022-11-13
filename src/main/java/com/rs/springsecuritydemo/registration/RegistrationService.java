package com.rs.springsecuritydemo.registration;

import com.rs.springsecuritydemo.user.User;
import com.rs.springsecuritydemo.user.UserRole;
import com.rs.springsecuritydemo.user.UserService;
import com.rs.springsecuritydemo.email.EmailSender;
import com.rs.springsecuritydemo.email.EmailService;
import com.rs.springsecuritydemo.token.ConfirmationToken;
import com.rs.springsecuritydemo.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailSender emailSender;
    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail){
            throw new IllegalStateException("EMail not valid.");
        }

        String token = userService.signupUser(
                new User(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        UserRole.USER
                )
        );

        String link = "http://localhost:8080/api/v1/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), EmailService.buildEmail(request.getFirstName(),link));
        return token;
    }

    @Transactional
    public String confirmToken(String token){
        ConfirmationToken confirmationToken =
                confirmationTokenService.getConfirmationToken(token).orElseThrow(()->
                        new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException("Email already confirmed.");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())){
            throw  new IllegalStateException("Token is expired.");
        }

        confirmationToken.setConfirmedAt(LocalDateTime.now());
        userService.enableAppUser(confirmationToken.getUser().getEmail());

        return "Token Confirmed";

    }


}
