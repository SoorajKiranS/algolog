package com.algolog.attendance_management.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@Getter
@Setter
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User employee;

    @Enumerated(EnumType.STRING)
    private WorkMode workMode; // OFFICE or WORK_FROM_HOME

    private LocalDateTime punchInTime;

    private LocalDateTime punchOutTime;

    private String reason;
}
