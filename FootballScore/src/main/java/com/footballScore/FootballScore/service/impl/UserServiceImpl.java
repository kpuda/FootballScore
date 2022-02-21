package com.footballScore.FootballScore.service.impl;

import com.footballScore.FootballScore.entity.Token;
import com.footballScore.FootballScore.entity.User;
import com.footballScore.FootballScore.enums.TokenType;
import com.footballScore.FootballScore.enums.TokenValid;
import com.footballScore.FootballScore.model.PasswordModel;
import com.footballScore.FootballScore.model.UserModel;
import com.footballScore.FootballScore.repository.TokenRepository;
import com.footballScore.FootballScore.repository.UserRepository;
import com.footballScore.FootballScore.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    TokenRepository tokenRepository;
    PasswordEncoder passwordEncoder;
    JavaMailSenderImpl javaMailSender;

    public UserServiceImpl(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, JavaMailSenderImpl javaMailSender) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Transactional
    public String registerUser(UserModel userModel, HttpServletRequest request) {
        User user = userRepository.findByEmail(userModel.getEmail());
        String url;
        if (user != null) {
            return "email_taken";
        } else {
            user = generateUser(userModel);
            String token = UUID.randomUUID().toString();
            Token registrationToken = new Token(user, token, TokenType.NEW_ACCOUNT_VERIFICATION, TokenValid.TOKEN_UNUSED);
            url = generateVerificationTokenUrl(generateUrl(request), registrationToken);
            userRepository.save(user);
            tokenRepository.save(registrationToken);
            // TODO sendEmailWithVerificationToken(user, url);
            log.info("Url: {}", url);
            log.info("Token: {}", token);
        }
        return "user_registered";
    }

    @Override
    @Transactional
    public String verifyRegistration(String token) {
        Token registrationToken = tokenRepository.findByToken(token);
        if (registrationToken == null) {
            return "no_token_found";
        } else {
            if (registrationToken.getExpirationDate().getTime() < new Date().getTime()) {
                return "token_expired";
            } else if (registrationToken.getTokenValid().equals(TokenValid.TOKEN_USED)) {
                return "token_used_already";
            } else {
                if (!registrationToken.getTokenType().equals(TokenType.NEW_ACCOUNT_VERIFICATION)) {
                    return "invalid_token_type";
                }

                User user = registrationToken.getUser();
                if (user.isEnabled()) {
                    return "user_verified_already";
                } else {
                    registrationToken.setTokenValid(TokenValid.TOKEN_USED);
                    user.setEnabled(true);
                    userRepository.save(user);
                    tokenRepository.save(registrationToken);
                }
            }
        }
        return "user_verified";
    }

    @Override
    public String changePasssword(PasswordModel passwordModel, HttpServletRequest request) {
        User user = userRepository.findByEmail(passwordModel.getEmail());
        String url;
        if (user == null) {
            return "no_user_found";
        } else {
            if (!checkIfOldPasswordIsValid(passwordModel.getPassword(), user)) {
                return "old_password_incorrect";
            } else {
                user.setPassword(passwordModel.getNewPassword());
                userRepository.save(user);
            }
        }
        return "password_changed";
    }

    @Override
    public String resetPassword(PasswordModel passwordModel, HttpServletRequest request) {
        User user = userRepository.findByEmail(passwordModel.getEmail());
        String url;
        if (user == null) {
            return "no_user_found";
        } else {
            String token = UUID.randomUUID().toString();
            Token resetPasswordToken = new Token(user, token, TokenType.FORGOT_PASSWORD, TokenValid.TOKEN_UNUSED);
            url = generateChangePasswordTokenUrl(generateUrl(request), resetPasswordToken);
            tokenRepository.save(resetPasswordToken);
            //TODO sendEmailWithResetPasswordToken(user,url);
            log.info("Url: {}", url);
            log.info("Token: {}", token);
        }
        return "reset_token_send";
    }

    @Override
    public String savePassword(String token, PasswordModel passwordModel) {
        Token changePasswordToken = tokenRepository.findByToken(token);
        if (changePasswordToken == null) {
            return "no_token_found";
        } else {
            if (changePasswordToken.getExpirationDate().getTime() < new Date().getTime()) {
                return "token_expired";
            } else if (changePasswordToken.getTokenValid().equals(TokenValid.TOKEN_USED)) {
                return "token_used_already";
            } else {
                User user = userRepository.findByEmail(changePasswordToken.getUser().getEmail());
                changePasswordToken.setTokenValid(TokenValid.TOKEN_USED);
                user.setPassword(passwordEncoder.encode(passwordModel.getNewPassword()));
                userRepository.save(user);
                tokenRepository.save(changePasswordToken);
            }
        }
        return "password_changed";
    }

    private void sendEmailWithVerificationToken(User user, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(user.getEmail());
        message.setSubject("Registration");
        message.setText(url);
        message.setReplyTo(user.getEmail());
        javaMailSender.send(message);
    }

    private void sendEmailWithResetPasswordToken(User user, String url) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("football@score.com");
        message.setTo(user.getEmail());
        message.setSubject("Reset password");
        message.setText(url);
        message.setReplyTo(user.getEmail());
        javaMailSender.send(message);
    }


    private User generateUser(UserModel userModel) {
        User user = new User();
        user.setEmail(userModel.getEmail());
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setPassword(passwordEncoder.encode(userModel.getPassword()));
        return user;
    }

    private String generateUrl(HttpServletRequest request) {
        return "http://"
                + request.getServerName() + ":" +
                request.getServerPort() +
                request.getContextPath();
    }

    private String generateVerificationTokenUrl(String applicationUrl, Token token) {
        return applicationUrl +
                "/verifyRegistration?token=" +
                token.getToken();
    }

    private String generateChangePasswordTokenUrl(String generateUrl, Token token) {
        return generateUrl
                + "/savePassword?token="
                + token.getToken();
    }

    private boolean checkIfOldPasswordIsValid(String oldPassword, User user) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}
