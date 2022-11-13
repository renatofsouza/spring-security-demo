package com.rs.springsecuritydemo.user;

import com.rs.springsecuritydemo.user.dto.UserRegistration;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users/registration")
@AllArgsConstructor
public class UserRegistrationController {
    private final UserService userService;

    @PostMapping
    public String registerNewUser(@RequestBody UserRegistration request){
        return userService.register(request);
    }

    @GetMapping(path = "confirmation")
    public String confirmNewUser(@RequestParam("token") String token){
        return userService.confirmToken(token);
    }


}
