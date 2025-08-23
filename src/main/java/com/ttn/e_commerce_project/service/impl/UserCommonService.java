package com.ttn.e_commerce_project.service.impl;

import com.ttn.e_commerce_project.entity.user.Role;
import com.ttn.e_commerce_project.enums.RoleAuthority;
import com.ttn.e_commerce_project.exceptionhandling.ResourceNotFoundException;
import com.ttn.e_commerce_project.respository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class UserCommonService {

    RoleRepository roleRepository;
    UserRepository userRepository;
    ActivationTokenServiceImpl verificationTokenService;
    EmailService emailService;
    TokenRepository tokenRepository;

    public Role findRoleByAuthority(RoleAuthority authority) {
        return roleRepository.findByAuthority(authority)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("No role found for authority : %s", authority)));
    }

    public AuthTokenVo login(UserLoginCo userLoginCo) {
        log.info("hello i am running");

        User user = userRepository.findByEmail(userLoginCo.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
}
