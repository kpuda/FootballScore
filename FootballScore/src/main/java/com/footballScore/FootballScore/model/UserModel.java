package com.footballScore.FootballScore.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String matchingPassword;
}
