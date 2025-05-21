package com.algolog.attendance_management.service;

import com.algolog.attendance_management.dtos.requestDtos.AttendanceMarkRequest;
import com.algolog.attendance_management.dtos.resposedtos.AttendanceResponse;
import com.algolog.attendance_management.entity.Attendance;
import com.algolog.attendance_management.entity.User;
import com.algolog.attendance_management.entity.WorkMode;
import com.algolog.attendance_management.repository.AttendanceRepository;
import com.algolog.attendance_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private UserRepository userRepository;

    public AttendanceResponse markAttendance(AttendanceMarkRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Attendance attendance = new Attendance();
        attendance.setEmployee(user);
        attendance.setWorkMode(request.getWorkMode());
        attendance.setPunchInTime(request.getPunchInTime());
        attendance.setPunchOutTime(request.getPunchOutTime());

        Attendance saved = attendanceRepository.save(attendance);
        return new AttendanceResponse(saved.getId(), user.getUsername(),
                saved.getPunchInTime(), saved.getPunchOutTime(), saved.getWorkMode());
    }

    public List<AttendanceResponse> getAttendanceByUser(Long userId) {
        return attendanceRepository.findByEmployeeId(userId).stream()
                .map(a -> new AttendanceResponse(a.getId(), a.getEmployee().getUsername(),
                        a.getPunchInTime(), a.getPunchOutTime(), a.getWorkMode()))
                .toList();
    }

    public List<AttendanceResponse> getAll() {
        return attendanceRepository.findAll().stream()
                .map(a -> new AttendanceResponse(a.getId(), a.getEmployee().getUsername(),
                        a.getPunchInTime(), a.getPunchOutTime(), a.getWorkMode()))
                .toList();
    }

    public List<AttendanceResponse> getAttendanceByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with username: " + username));

        return attendanceRepository.findByEmployeeId(user.getId()).stream()
                .map(a -> new AttendanceResponse(
                        a.getId(),
                        user.getUsername(),
                        a.getWorkMode(),
                        a.getPunchInTime(),
                        a.getPunchOutTime()
                ))
                .toList();
    }

    public AttendanceResponse markAttendanceByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();

        Optional<Attendance> existingAttendance = attendanceRepository.findByEmployeeAndPunchInTimeBetweenAndPunchOutTimeIsNull(
                user,
                today.atStartOfDay(),
                today.atTime(LocalTime.MAX)
        );

        if (existingAttendance.isPresent()) {
            // Return existing attendance details (user already punched in)
            Attendance attendance = existingAttendance.get();
            return new AttendanceResponse(
                    attendance.getId(),
                    user.getUsername(),
                    attendance.getWorkMode(),
                    attendance.getPunchInTime(),
                    attendance.getPunchOutTime()
            );
        }

        // Else create new attendance record
        Attendance attendance = new Attendance();
        attendance.setEmployee(user);
        attendance.setWorkMode(WorkMode.OFFICE);  // or get dynamically
        attendance.setPunchInTime(LocalDateTime.now());
        attendanceRepository.save(attendance);

        return new AttendanceResponse(
                attendance.getId(),
                user.getUsername(),
                attendance.getWorkMode(),
                attendance.getPunchInTime(),
                attendance.getPunchOutTime()
        );
    }

    public List<AttendanceResponse> markAttendanceForUsers(List<String> usernames) {
        return usernames.stream()
                .map(username -> {
                    try {
                        // Reuse your existing method to mark attendance by username
                        return markAttendanceByUsername(username);
                    } catch (RuntimeException e) {
                        // If marking attendance fails (e.g., user not found), return null or some error response
                        // Here returning null, you can improve by custom error object if needed
                        return null;
                    }
                })
                .filter(response -> response != null) // remove failed/null entries
                .toList();
    }

    public AttendanceResponse markPunchOutByUsername(String username) {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = today.atStartOfDay(); // 00:00
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX); // 23:59:59.999999999

        Attendance attendance = attendanceRepository
                .findTodayActiveAttendanceByUsername(username, startOfDay, endOfDay)
                .orElseThrow(() -> new RuntimeException("No punch-in record found for today."));

        attendance.setPunchOutTime(LocalDateTime.now());
        attendanceRepository.save(attendance);

        return new AttendanceResponse(attendance);
    }

    public AttendanceResponse markPunchInByUsername(String username, WorkMode workMode, String reason) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        LocalDate today = LocalDate.now();

        Optional<Attendance> existingAttendance = attendanceRepository.findByEmployeeAndPunchInTimeBetweenAndPunchOutTimeIsNull(
                user,
                today.atStartOfDay(),
                today.atTime(LocalTime.MAX)
        );

        if (existingAttendance.isPresent()) {
            Attendance attendance = existingAttendance.get();
            return new AttendanceResponse(
                    attendance.getId(),
                    user.getUsername(),
                    attendance.getWorkMode(),
                    attendance.getPunchInTime(),
                    attendance.getPunchOutTime(),
                    attendance.getReason()
            );
        }

        Attendance attendance = new Attendance();
        attendance.setEmployee(user);
        attendance.setWorkMode(workMode);
        attendance.setPunchInTime(LocalDateTime.now());
        attendance.setReason(reason);
        attendanceRepository.save(attendance);

        return new AttendanceResponse(
                attendance.getId(),
                user.getUsername(),
                attendance.getWorkMode(),
                attendance.getPunchInTime(),
                attendance.getPunchOutTime()
        );
    }



}

