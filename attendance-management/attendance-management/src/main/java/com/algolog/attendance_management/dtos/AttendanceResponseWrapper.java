package com.algolog.attendance_management.dtos;

import com.algolog.attendance_management.dtos.resposedtos.AttendanceResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceResponseWrapper {
    private String responseId;
    private String sourceSystem;
    private String timeStamp;
    private List<AttendanceResponse> payLoad;
    private String status;
}
