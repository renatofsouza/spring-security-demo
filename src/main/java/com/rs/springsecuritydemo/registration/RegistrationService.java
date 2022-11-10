package com.rs.springsecuritydemo.registration;

import com.rs.springsecuritydemo.appuser.AppUser;
import com.rs.springsecuritydemo.appuser.AppUserRole;
import com.rs.springsecuritydemo.appuser.AppUserService;
import com.rs.springsecuritydemo.registration.token.ConfirmationToken;
import com.rs.springsecuritydemo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;

    private final AppUserService appUserService;

    private final ConfirmationTokenService confirmationTokenService;
    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if (!isValidEmail){
            throw new IllegalStateException("EMail not valid.");
        }

        return appUserService.signupUser(
                new AppUser(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getPassword(),
                        AppUserRole.USER
                )
        );
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
        appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());

        return "Token Confirmed";

    }



}
