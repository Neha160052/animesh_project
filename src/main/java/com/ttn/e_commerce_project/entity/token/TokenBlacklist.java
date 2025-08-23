package com.ttn.e_commerce_project.entity.token;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;


@Entity
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(nullable = false, unique = true)
    String jti;
    @Column(nullable = false, unique = true)
    String refreshJti;

    public TokenBlacklist(String jti, String refreshJti) {
        this.jti=jti;
        this.refreshJti=refreshJti;
    }
}