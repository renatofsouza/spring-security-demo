package com.rs.springsecuritydemo.user;

import com.rs.springsecuritydemo.registration.token.ConfirmationToken;
import com.rs.springsecuritydemo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final static String USER_NOT_FOUND_MSG ="User with e-mail %s not found.";
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email).orElseThrow(()->
                new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG,email)));
    }

    @Transactional
    public String signupUser(User user){

        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if(userExists){
            //TODO: If user already exists but has not confirmed e-mail, then send another token.
            throw new IllegalStateException("Email already taken. ");

        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken =
                new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        userRepository.save(user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public void enableAppUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalStateException("Unable to find user during token authentication."));

        user.setEnabled(true);
        userRepository.save(user);
    }
}
