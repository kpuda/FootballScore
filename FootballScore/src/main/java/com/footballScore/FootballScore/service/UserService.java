package com.footballScore.FootballScore.service;

import com.footballScore.FootballScore.model.PasswordModel;
import com.footballScore.FootballScore.model.UserModel;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    String registerUser(UserModel userModel, HttpServletRequest request);

    String verifyRegistration(String token);

    String changePasssword(PasswordModel passwordModel, HttpServletRequest request);

    String savePassword(String token, PasswordModel passwordModel);

    String resetPassword(PasswordModel passwordModel, HttpServletRequest request);
}
