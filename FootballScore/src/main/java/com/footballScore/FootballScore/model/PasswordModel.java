package com.footballScore.FootballScore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordModel {

    private String email;
    private String password;
    private String newPassword;
}
