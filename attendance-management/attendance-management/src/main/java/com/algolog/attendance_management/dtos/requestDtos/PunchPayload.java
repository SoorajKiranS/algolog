package com.algolog.attendance_management.dtos.requestDtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PunchPayload {
    private String workMode;
    private String reason;
}
