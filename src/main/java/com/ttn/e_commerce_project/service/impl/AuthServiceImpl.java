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
import java.time.ZonedDateTime;

import static com.ttn.e_commerce_project.constants.UserConstants.*;
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
        log.info(LOGIN_INITIATED);

        User user = userCommonService.findUserByEmail(userLoginCo.getEmail());

        if(!user.isActive())
            throw  new AccountNotActiveException(ACCOUNT_NOT_ACTIVE);

        if (user.isLocked()) {
            if (unlockWhenTimeExpired(user)) {
                user.setLocked(false);
                user.setInvalidAttemptCount(0);
                userRepository.save(user);
            } else {
                emailService.sendAcknowledgementMail(userLoginCo.getEmail(), ACCOUNT_LOCKED_MAIL);
                throw new AccountLockedException(ACCOUNT_LOCKED);
            }
            log.info(LOGIN_SUCCESSFUL);
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
            if(user.isLocked())
                throw new AccountLockedException(ACCOUNT_LOCKED_LIMIT_REACHED);
            throw new BadCredentialsException(INVALID_USERNAME_PASSWORD);
        }
    }

    public void logout(String accessToken,String refreshToken) {
        jwtService.validateToken(accessToken);
        jwtService.validateRefreshToken(refreshToken);
        String jti = jwtUtil.getJtiFromToken(accessToken);
        String refreshJti = jwtUtil.getJtiFromToken(refreshToken);
        TokenBlacklist blacklist = new TokenBlacklist(jti,refreshJti);
        tokenBlacklistRepository.save(blacklist);

        log.info(TOKEN_BLACKLISTED, jti,refreshJti);
    }

    @Override
    public void initiatePasswordReset(String email) {
        User user = userCommonService.findUserByEmail(email);
        userCommonService.verifyUser(user.getEmail());
        {
        VerificationToken token = tokenService.createToken(user);
        emailService.sendLinkWithSubjectEmail(email, userCommonService.activationLink(token), PASSWORD_RESET_SUBJECT);}
    }

    @Override
    public void resetUserPassword(String email,String password, String confirmPassword)
    {
        userCommonService.verifyUser(email);
        try {
            if(password.matches(confirmPassword)) {
                userRepository.updatePassword(email, passwordEncoder.encode(password));
                User user = userCommonService.findUserByEmail(email);
                user.setPasswordUpdateDate(ZonedDateTime.now());
            }
        } catch (PasswordMismatchException e) {
            throw new PasswordMismatchException(PASSWORD_MISMATCH);
        }
    }

    public void increaseFailedAttempts(User user) {
        int invalidAttempts = user.getInvalidAttemptCount()+1;
        user.setInvalidAttemptCount(invalidAttempts);

        if(invalidAttempts >= 3)
        {
            user.setLocked(true);
            user.setLockTime(LocalDateTime.now());
        }
        userRepository.save(user);
    }
//  To Do : send email to the user to stating that the account has been locked

    public void resetFailedAttempts(User user) {
        user.setInvalidAttemptCount(0);
        userRepository.save(user);
    }

    public boolean unlockWhenTimeExpired(User user) {
        if(user.getLockTime()==null)
        { log.warn(LOCK_TIME_NULL);
            return false;}
        LocalDateTime unlockTime = user.getLockTime().plusSeconds(Long.parseLong(lockTimeDuration));
        if(LocalDateTime.now().isAfter(unlockTime))
            return true;
        else
            return false;
    }

    public String generateNewAccessToken(String refreshToken)
    {
        if (!(jwtService.validateRefreshToken(refreshToken))) {
            throw new InvalidArgumentException(TOKEN_INVALID_OR_EXPIRED);
        }

        String refreshJti = jwtUtil.getJtiFromToken(refreshToken);
        if (tokenBlacklistRepository.existsByJti(refreshJti)) {
            throw new InvalidArgumentException(REFRESH_TOKEN_BLACKLISTED);
        }
        String username = jwtService.getUsername(refreshToken);
        CustomUserDetails userDetails = (CustomUserDetails) userDetailService.loadUserByUsername(username);
        return jwtUtil.generateAccessToken(userDetails);
    }
}

