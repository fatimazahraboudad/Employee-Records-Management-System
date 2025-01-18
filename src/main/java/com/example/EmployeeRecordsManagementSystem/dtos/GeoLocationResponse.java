package com.example.EmployeeRecordsManagementSystem.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class GeoLocationResponse {
    private String status;
    private String city;
    private String country;
    private String regionName;
}