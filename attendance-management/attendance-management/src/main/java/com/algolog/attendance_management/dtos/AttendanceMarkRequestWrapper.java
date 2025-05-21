package com.algolog.attendance_management.dtos;

import com.algolog.attendance_management.dtos.requestDtos.AttendanceMarkRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AttendanceMarkRequestWrapper {

    private String requestId;
    private String sourceSystem;
    private String timeStamp;
    private List<AttendanceMarkRequest> payLoad;
}
