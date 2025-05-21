package com.algolog.attendance_management.dtos.resposedtos;

import com.algolog.attendance_management.entity.Attendance;
import com.algolog.attendance_management.entity.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponse {
    private Long attendanceId;
    private String username;
    private LocalDateTime punchInTime;
    private LocalDateTime punchOutTime;
    private WorkMode workMode;

    public AttendanceResponse(Long attendanceId, String username, WorkMode workMode, LocalDateTime punchInTime, LocalDateTime punchOutTime) {
        this.attendanceId = attendanceId;
        this.username = username;
        this.workMode = workMode;
        this.punchInTime = punchInTime;
        this.punchOutTime = punchOutTime;
    }

    public AttendanceResponse(Attendance attendance) {
    }

    public AttendanceResponse(Long id, String username, WorkMode workMode, LocalDateTime punchInTime, LocalDateTime punchOutTime, String reason) {
    }
}
