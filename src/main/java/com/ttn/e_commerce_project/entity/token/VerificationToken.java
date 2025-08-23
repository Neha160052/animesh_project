package com.ttn.e_commerce_project.entity.token;

import com.ttn.e_commerce_project.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Getter
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String token;
    LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name= "user_id",nullable = false)
    User user;

    public VerificationToken(String token,User user,int minutesToExpire) {
        this.token = token;
        this.expiryDate = LocalDateTime.now().plusMinutes(minutesToExpire);
        this.user = user;
    }

    public boolean isExpire()
    {
        return LocalDateTime.now().isAfter(expiryDate);
    }
}
