package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.co.ResetPasswordCo;
import com.ttn.e_commerce_project.dto.co.UserLoginCo;
import com.ttn.e_commerce_project.dto.vo.AuthTokenVo;
import com.ttn.e_commerce_project.service.impl.AuthServiceImpl;
import com.ttn.e_commerce_project.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AuthController{

    AuthServiceImpl authServiceImpl;
    JwtUtil jwtUtil;
      @PostMapping("/login")
      public ResponseEntity<AuthTokenVo> login(@Valid @RequestBody UserLoginCo userLoginCo)
      {
          AuthTokenVo token  = authServiceImpl.login(userLoginCo);
          return  ResponseEntity.ok(token);
      }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader String accessToken,@RequestBody String refreshToken) {
        authServiceImpl.logout(accessToken, refreshToken);
        return ResponseEntity.ok("Logged out successfully");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> initiateResetPassword(@RequestBody String email)
    {
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
    public ResponseEntity<String> generateToken(@RequestBody String token)
    {
        String newAccessToken = authServiceImpl.generateNewAccessToken(token);
        return ResponseEntity.ok(newAccessToken);
    }
}
