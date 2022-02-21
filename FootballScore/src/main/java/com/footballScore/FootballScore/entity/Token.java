package com.footballScore.FootballScore.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Token {

    private static final int EXPIRATION_TIME = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String token;
    private Date expirationDate;
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_VERIFY_TOKEN"))
    private User user;

    public Token(User user, String token,TokenType tokenType) {
        super();
        this.token = token;
        this.user = user;
        this.expirationDate = generateExpirationDate(EXPIRATION_TIME);
        this.tokenType=tokenType;
    }

    private Date generateExpirationDate(int expirationDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, expirationDate);
        return new Date(calendar.getTime().getTime());
    }
}
