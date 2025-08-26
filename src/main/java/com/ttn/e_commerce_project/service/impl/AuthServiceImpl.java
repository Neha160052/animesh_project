package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.dto.co.UserLoginCo;
import com.ttn.e_commerce_project.dto.vo.AuthTokenVo;
import com.ttn.e_commerce_project.entity.token.TokenBlacklist;
import com.ttn.e_commerce_project.entity.token.VerificationToken;
import com.ttn.e_commerce_project.entity.user.CustomUserDetails;
import com.ttn.e_commerce_project.entity.user.User;
import com.ttn.e_commerce_project.exceptionhandling.AccountLockedException;
import com.ttn.e_commerce_project.exceptionhandling.AccountNotActiveException;
import com.ttn.e_commerce_project.exceptionhandling.InvalidArgumentException;
import com.ttn.e_commerce_project.exceptionhandling.PasswordMismatchException;
import com.ttn.e_commerce_project.respository.TokenBlacklistRepository;
import com.ttn.e_commerce_project.respository.UserRepository;
import com.ttn.e_commerce_project.service.AuthService;
import com.ttn.e_commerce_project.service.EmailService;
import com.ttn.e_commerce_project.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE)
public class AuthServiceImpl implements AuthService {

    final AuthenticationManager authenticationManager;
    final JwtUtil jwtUtil;
    final JwtService jwtService;
    final UserRepository userRepository;
    final TokenBlacklistRepository tokenBlacklistRepository;
    final UserCommonService userCommonService;
    final EmailService emailService;
    final TokenServiceImpl tokenService;
    final UserDetailServiceImpl userDetailService;
    final PasswordEncoder passwordEncoder;

    @Value("${account.lock.time}")
    String lockTimeDuration;

    public AuthTokenVo login(UserLoginCo userLoginCo) {
        log.info("login initiated");

        User user = userCommonService.findUserByEmail(userLoginCo.getEmail());

        if(!user.isActive())
            throw  new AccountNotActiveException("Account not active");

        if (user.isLocked()) {
            if (unlockWhenTimeExpired(user)) {
                user.setLocked(false);
                user.setInvalidAttemptCount(0);
                userRepository.save(user);
            } else {
                throw new AccountLockedException("Account is locked. Please try again after sometime");
            }
            log.info("login successful");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginCo.getEmail(),userLoginCo.getPassword()));
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            resetFailedAttempts(user);
            String token = jwtUtil.generateAccessToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            return AuthTokenVo.builder().refreshToken(refreshToken).accessToken(token).build();
        } catch (BadCredentialsException e) {
            increaseFailedAttempts(user);
            throw new BadCredentialsException("You have reached the maximum limit of login attempts");
        }
    }

    public void logout(String accessToken,String refreshToken) {
        jwtService.validateToken(accessToken);
        jwtService.validateToken(refreshToken);
        String jti = jwtUtil.getJtiFromToken(accessToken);
        String refreshJti = jwtUtil.getJtiFromToken(refreshToken);
        TokenBlacklist blacklist = new TokenBlacklist(jti,refreshJti);
        tokenBlacklistRepository.save(blacklist);

        log.info("Token with jti={} refreshJti={} has been blacklisted", jti,refreshJti);
    }

    @Override
    public void initiatePasswordReset(String email) {
        User user = userCommonService.findUserByEmail(email);
        if(!user.isActive())
            throw  new AccountNotActiveException("Account not active");
        VerificationToken token = tokenService.createToken(user);
        emailService.sendResetPasswordEmail(email, userCommonService.activationLink(token));
    }

    @Override
    public void resetUserPassword(String email,String password, String confirmPassword)
    {
        try {
            if(password.matches(confirmPassword))
                userRepository.updatePassword(email,passwordEncoder.encode(password));
        } catch (PasswordMismatchException e) {
            throw new PasswordMismatchException("Password and Confirm password should match");
        }
    }

    public void increaseFailedAttempts(User user) {
        int invalidAttempts = user.getInvalidAttemptCount()+1;
        user.setInvalidAttemptCount(invalidAttempts);

        if(invalidAttempts >= 3)
        {
            user.setLocked(true);
            user.setLockTime(LocalDateTime.now());
            userRepository.save(user);
        }
    }
//  To Do : send email to the user to stating that the account has been locked

    public void resetFailedAttempts(User user) {
        user.setInvalidAttemptCount(0);
        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        if(user.getLockTime()==null)
            return false;
        LocalDateTime unlockTime = user.getLockTime().plusSeconds(Long.parseLong(lockTimeDuration));
        if(LocalDateTime.now().isAfter(unlockTime))
            return true;
        else
            return false;
    }

    public String generateNewAccessToken(String refreshToken)
    {
        if (!(jwtService.validateToken(refreshToken))) {
            throw new InvalidArgumentException("Invalid or expired refresh token");
        }

        String refreshJti = jwtUtil.getJtiFromToken(refreshToken);
        if (tokenBlacklistRepository.existsByJti(refreshJti)) {
            throw new InvalidArgumentException("Refresh token blacklisted please login again");
        }
        String username = jwtService.getUsername(refreshToken);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(username);
        return jwtUtil.generateAccessToken(userDetails);
    }
}

