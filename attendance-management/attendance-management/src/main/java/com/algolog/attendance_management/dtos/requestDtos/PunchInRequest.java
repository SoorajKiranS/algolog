package com.algolog.attendance_management.dtos.requestDtos;

import com.algolog.attendance_management.entity.WorkMode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PunchInRequest {
    private WorkMode workMode;
}

