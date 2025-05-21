package com.algolog.attendance_management.dtos.requestDtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PunchRequestWrapper {
    private String requestId;
    private String sourceSystem;
    private String timeStamp;
    private List<PunchPayload> payLoad;
}
