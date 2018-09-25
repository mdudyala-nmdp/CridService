package com.nmdp.cridservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)

public class PatientRequest {
    Integer ccn;
    PatientRequestInfo patient;
}
