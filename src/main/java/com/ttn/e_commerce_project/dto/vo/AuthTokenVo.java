package com.ttn.e_commerce_project.dto.vo;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthTokenVo{

       String accessToken;
       String refreshToken;
}
