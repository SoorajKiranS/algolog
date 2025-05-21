package com.algolog.attendance_management.controller;

import com.algolog.attendance_management.dtos.AttendanceResponseWrapper;
import com.algolog.attendance_management.dtos.requestDtos.PunchRequestWrapper;
import com.algolog.attendance_management.dtos.resposedtos.AttendanceResponse;
import com.algolog.attendance_management.entity.WorkMode;
import com.algolog.attendance_management.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @PostMapping("/punch-in")
    @PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN')")
    public ResponseEntity<AttendanceResponseWrapper> punchIn(
            Authentication auth,
            @RequestBody PunchRequestWrapper requestWrapper) {

        String username = auth.getName();

        String workModeStr = requestWrapper.getPayLoad().get(0).getWorkMode();
        String reason = requestWrapper.getPayLoad().get(0).getReason();

        WorkMode workMode = WorkMode.valueOf(workModeStr.toUpperCase().replace(" ", "_"));

        AttendanceResponse res = attendanceService.markPunchInByUsername(username, workMode, reason);

        AttendanceResponseWrapper wrapper = new AttendanceResponseWrapper();
        wrapper.setResponseId(UUID.randomUUID().toString());
        wrapper.setSourceSystem("Algolog");
        wrapper.setTimeStamp(LocalDateTime.now().toString());
        wrapper.setPayLoad(List.of(res));
        wrapper.setStatus("SUCCESS");

        return ResponseEntity.ok(wrapper);
    }



    // Get attendance records of logged-in user
    @GetMapping("/my")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<AttendanceResponseWrapper> getMyAttendance(Authentication authentication) {
        String username = authentication.getName();
        List<AttendanceResponse> attendanceList = attendanceService.getAttendanceByUsername(username);

        AttendanceResponseWrapper responseWrapper = new AttendanceResponseWrapper();
        responseWrapper.setResponseId(UUID.randomUUID().toString());
        responseWrapper.setSourceSystem("Algolog");
        responseWrapper.setTimeStamp(LocalDateTime.now().toString());
        responseWrapper.setPayLoad(attendanceList);
        responseWrapper.setStatus("SUCCESS");

        return ResponseEntity.ok(responseWrapper);
    }

    // Get attendance records of all users - admin only
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttendanceResponseWrapper> getAll() {
        List<AttendanceResponse> attendanceList = attendanceService.getAll();

        AttendanceResponseWrapper responseWrapper = new AttendanceResponseWrapper();
        responseWrapper.setResponseId(UUID.randomUUID().toString());
        responseWrapper.setSourceSystem("Algolog");
        responseWrapper.setTimeStamp(LocalDateTime.now().toString());
        responseWrapper.setPayLoad(attendanceList);
        responseWrapper.setStatus("SUCCESS");

        return ResponseEntity.ok(responseWrapper);
    }

    @PostMapping("/punch-out")
    @PreAuthorize("permitAll()")
    public ResponseEntity<AttendanceResponseWrapper> punchOut(Authentication auth) {
        String username = auth.getName();
        AttendanceResponse res = attendanceService.markPunchOutByUsername(username);

        AttendanceResponseWrapper wrapper = new AttendanceResponseWrapper();
        wrapper.setResponseId(UUID.randomUUID().toString());
        wrapper.setSourceSystem("Algolog");
        wrapper.setTimeStamp(LocalDateTime.now().toString());
        wrapper.setStatus("SUCCESS");

        return ResponseEntity.ok(wrapper);
    }

}
