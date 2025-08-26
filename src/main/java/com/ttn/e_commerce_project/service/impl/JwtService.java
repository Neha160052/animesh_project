package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.security.JwtFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    Key secretKey;

    public JwtService(@Value("${secret.key}")String secretKey)
    {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)   // your secret key for verification
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            if (expiration.before(new Date())) {
                return false;
            }
            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return true;
        }

    }

    public String getUsername(String token) {

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // username is stored in subject
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidArgumentException("Invalid or expired token");
        }
    }
    public String getRoles(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("roles",String.class);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidArgumentException("Invalid or expired token");
        }
    }

}
