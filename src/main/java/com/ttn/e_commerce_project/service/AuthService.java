package com.ttn.e_commerce_project.service;

import com.ttn.e_commerce_project.dto.co.UserLoginCo;
import com.ttn.e_commerce_project.dto.vo.AuthTokenVo;

public interface AuthService {

     AuthTokenVo login(UserLoginCo userLoginCo);
     void logout(String accessToken,String refreshToken);
     void initiatePasswordReset(String email);
     void resetUserPassword(String email,String password, String confirmPassword);
}
