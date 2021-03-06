package com.footballScore.FootballScore.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String email;

    @Column(length = 60)
    private String password;
    private String firstName;
    private String lastName;
    private boolean isEnabled = false;
}
