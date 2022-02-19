package com.footballScore.FootballScore.service.impl;

import com.footballScore.FootballScore.entity.RegistrationToken;
import com.footballScore.FootballScore.entity.User;
import com.footballScore.FootballScore.model.UserModel;
import com.footballScore.FootballScore.repository.RegistrationTokenRepository;
import com.footballScore.FootballScore.repository.UserRepository;
import com.footballScore.FootballScore.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    RegistrationTokenRepository registrationTokenRepository;

    @Override
    @Transactional
    public String registerUser(UserModel userModel, HttpServletRequest request) {
        User user = userRepository.findByEmail(userModel.getEmail());
        if (user != null) {
            return "email_taken";
        } else {
            user.setEmail(userModel.getEmail());
            user.setFirstName(userModel.getFirstName());
            user.setLastName(userModel.getLastName());

            RegistrationToken registrationToken = new RegistrationToken();
            registrationToken.setToken(UUID.randomUUID().toString());
            registrationToken.setUser(user);

            userRepository.save(user);
            registrationTokenRepository.save(registrationToken);
        }
        return "user_registered";
    }
}
