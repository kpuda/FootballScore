package com.footballScore.FootballScore.controller;

import com.footballScore.FootballScore.model.PasswordModel;
import com.footballScore.FootballScore.model.UserModel;
import com.footballScore.FootballScore.service.impl.UserServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private final UserServiceImpl userService;

    @GetMapping("/hello")
    public String hello(){
        return "Hello";
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request){
        return userService.registerUser(userModel,request);
    }

    @PostMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
        return userService.verifyRegistration(token);
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel, final  HttpServletRequest request){
        return userService.changePasssword(passwordModel,request);
    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel,final HttpServletRequest request){
        return userService.resetPassword(passwordModel,request);
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel){
        return userService.savePassword(token,passwordModel);
    }

}
