package com.algolog.attendance_management.dtos.resposedtos;

import com.algolog.attendance_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private Role role;

}

