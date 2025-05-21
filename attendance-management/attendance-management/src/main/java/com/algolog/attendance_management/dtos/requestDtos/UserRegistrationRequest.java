package com.algolog.attendance_management.dtos.requestDtos;

import com.algolog.attendance_management.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
    private String username;
    private String password;
    private Role role;
}
