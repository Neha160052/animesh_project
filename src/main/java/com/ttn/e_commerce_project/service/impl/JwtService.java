package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.security.JwtFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class JwtService {

    Key secretKey;

    public JwtService(@Value("${secret.key}")String secretKey)
    {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public boolean validateToken(String token) {
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> validating the jwt token");
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Date expiration = claims.getExpiration();
            boolean notExpired = expiration.after(new Date());
            String tokenType = claims.get("type", String.class);
            boolean isAccessToken = "ACCESS".equalsIgnoreCase(tokenType);
            if(notExpired && isAccessToken){
                log.info(">>>>>>>>>>>>>>>>>>>>>>Returning true as the access token in valid");
                return true;}
            log.warn(">>>>>>>>>>>>>>>>>>>>>>>Token is either expired or type mismatch");
            return false;
        } catch (JwtException | InvalidArgumentException e) {
            log.info(">>>>>>>>>>>>>>>>>>>> throwing exception because the token is not valid or it is expired");
            return false;
        }}

        public boolean validateRefreshToken(String token) {
            try {
                log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> validating the jwt token");
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                Date expiration = claims.getExpiration();
                boolean notExpired = expiration.after(new Date());
                String tokenType = claims.get("type", String.class);
                boolean isRefreshToken = "REFRESH".equalsIgnoreCase(tokenType);
                if(notExpired && isRefreshToken){
                    log.info(">>>>>>>>>>>>>>>>>>>>>>Returning true as the refresh token in valid");
                    return true;}
                log.warn(">>>>>>>>>>>>>>>>>>>>>>>Token is either expired or type mismatch");
                return false;

            } catch (JwtException | InvalidArgumentException e) {
                log.info(">>>>>>>>>>>>>>>>>>>> throwing exception because the token is not valid or it is expired");
                return false;
            }
    }

    public String getUsername(String token) {

        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> retrieving username from  the jwt token");
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // username is stored in subject
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidArgumentException("username does not exist");
        }
    }
    public List<String> getRoles(String token) {
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>> retrieving roles from  the jwt token");
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("roles",List.class);
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidArgumentException("Role does not exist");
        }
    }
}
