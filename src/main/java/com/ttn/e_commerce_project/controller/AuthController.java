package com.ttn.e_commerce_project.controller;

import com.ttn.e_commerce_project.dto.co.UserLoginCo;
import com.ttn.e_commerce_project.service.impl.CustomerServiceImpl;
import com.ttn.e_commerce_project.service.impl.UserCommonService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AuthController {

    UserCommonService userCommonService;

      @PostMapping("/login")
      public String login(@Valid @RequestBody UserLoginCo userLoginCo)
      {
          AuthTokenVo token  = userCommonService.login(userLoginCo);
          return  ResponseEntity.ok(token);
      }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody AuthTokenVo authTokenVo) {

        String accessToken = authTokenVo.getAccessToken();
        String refreshToken = authTokenVo.getRefreshToken();
        userCommonService.logout(accessToken, refreshToken);

        return ResponseEntity.ok("Logged out successfully");
    }
}
