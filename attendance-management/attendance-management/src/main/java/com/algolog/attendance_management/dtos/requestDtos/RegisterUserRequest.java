package com.algolog.attendance_management.dtos.requestDtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterUserRequest {

    private String username;
    private String password;
    private String role;
    private String email;
    private String mobile;
}
