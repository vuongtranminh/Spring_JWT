package com.tranminhvuong.jwt.payloads;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * DTO: Data Transfer Object
 */
@Getter
@Setter
@ToString
public class LoginRequest {

    @NotEmpty(message = "Username must not be empty")
    @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
    private String username;

    @NotEmpty(message = "Password must not be empty")
    @Size(min = 3, max = 30, message = "Password must be between 3 and 30 characters")
    private String password;

}
