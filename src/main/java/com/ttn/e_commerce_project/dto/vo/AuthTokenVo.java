package com.ttn.e_commerce_project.dto.vo;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthTokenVo {
    private String accessToken;
    private String refreshToken;
}
