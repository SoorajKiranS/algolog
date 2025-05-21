package com.algolog.attendance_management.dtos.requestDtos;

import com.algolog.attendance_management.entity.WorkMode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
@Getter
@Setter
public class AttendanceMarkRequest {
    private Long userId;
    private LocalDateTime punchInTime;
    private LocalDateTime punchOutTime;
    private WorkMode workMode;
}
