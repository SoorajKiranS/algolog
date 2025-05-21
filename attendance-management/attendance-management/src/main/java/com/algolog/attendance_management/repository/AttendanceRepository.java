package com.algolog.attendance_management.repository;

import com.algolog.attendance_management.entity.Attendance;
import com.algolog.attendance_management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByEmployeeId(Long employeeId);

    boolean existsByEmployeeAndPunchInTimeBetweenAndPunchOutTimeIsNull(
            User employee, LocalDateTime start, LocalDateTime end);

    Optional<Attendance> findByEmployeeAndPunchInTimeBetweenAndPunchOutTimeIsNull(
            User employee, LocalDateTime start, LocalDateTime end);

    @Query("SELECT a FROM Attendance a WHERE a.employee.username = :username AND a.punchInTime BETWEEN :start AND :end")
    Optional<Attendance> findTodayActiveAttendanceByUsername(@Param("username") String username,
                                                             @Param("start") LocalDateTime start,
                                                             @Param("end") LocalDateTime end);

}
