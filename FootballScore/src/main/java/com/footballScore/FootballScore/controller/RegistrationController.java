package com.footballScore.FootballScore.controller;

import com.footballScore.FootballScore.model.UserModel;
import com.footballScore.FootballScore.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        return userService.registerUser(userModel,request);
    }

    @PostMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        return userService.verifyRegistration(token);
    }

}
