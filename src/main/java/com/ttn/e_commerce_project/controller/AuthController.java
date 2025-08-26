package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.co.ResetPasswordCo;
import com.ttn.e_commerce_project.dto.co.UserLoginCo;
import com.ttn.e_commerce_project.dto.vo.AuthTokenVo;
import com.ttn.e_commerce_project.service.impl.AuthServiceImpl;
import com.ttn.e_commerce_project.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AuthController{

    AuthServiceImpl authServiceImpl;

      @PostMapping("/login")
      public ResponseEntity<AuthTokenVo> login(@Valid @RequestBody UserLoginCo userLoginCo)
      {
          AuthTokenVo token  = authServiceImpl.login(userLoginCo);
          return  ResponseEntity.ok(token);
      }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, @RequestParam String refreshToken) {
        String authHeader = request.getHeader("Authorization");
        String accessToken = null;
        if (nonNull(authHeader) && authHeader.startsWith("Bearer ")) {
            accessToken = authHeader.substring(7);
        }
        authServiceImpl.logout(accessToken, refreshToken);
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/forgot-password")
    public ResponseEntity<String> initiateResetPassword(@RequestParam String email) {
            authServiceImpl.initiatePasswordReset(email);
            return ResponseEntity.ok("reset password email has been sent");
        }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordCo resetPasswordCo)
    {
        String email = resetPasswordCo.getEmail();
        String password = resetPasswordCo.getPassword();
        String confirmPassword = resetPasswordCo.getConfirmPassword();
        authServiceImpl.resetUserPassword(email,password,confirmPassword);
        return ResponseEntity.ok("Password reset successful");
    }

    @PostMapping("/generate-new-access-token")
    public ResponseEntity<String> generateToken(@RequestParam String token)
    {
        String newAccessToken = authServiceImpl.generateNewAccessToken(token);
        return ResponseEntity.ok(newAccessToken);
    }
}
