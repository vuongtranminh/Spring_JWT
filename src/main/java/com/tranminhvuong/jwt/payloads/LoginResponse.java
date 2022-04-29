package com.tranminhvuong.jwt.payloads;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class LoginResponse {

    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private String email;
    private List<String> roles;

    public LoginResponse(String accessToken, String username, String email, List<String> roles) {
        this.accessToken = accessToken;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}
