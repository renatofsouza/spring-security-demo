package com.rs.springsecuritydemo.registration;

import com.rs.springsecuritydemo.user.UserService;
import com.rs.springsecuritydemo.user.dto.UserRegistration;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;
    private final UserService userService;

    @PostMapping
    public String register (@RequestBody UserRegistration request){
        return userService.register(request);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token){
        return registrationService.confirmToken(token);
    }


}
