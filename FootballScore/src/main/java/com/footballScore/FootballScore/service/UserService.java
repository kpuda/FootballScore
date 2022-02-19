package com.footballScore.FootballScore.service;

import com.footballScore.FootballScore.model.UserModel;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    String registerUser(UserModel userModel, HttpServletRequest request);
}
