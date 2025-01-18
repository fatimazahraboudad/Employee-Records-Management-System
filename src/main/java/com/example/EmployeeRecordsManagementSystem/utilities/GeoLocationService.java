package com.example.EmployeeRecordsManagementSystem.utilities;

import com.example.EmployeeRecordsManagementSystem.dtos.GeoLocationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeoLocationService {
    private static final String GEOLOCATION_API_URL = "http://ip-api.com/json/";

    public String getCityFromIP(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        String url = GEOLOCATION_API_URL + ip;
        try {
            GeoLocationResponse response = restTemplate.getForObject(url, GeoLocationResponse.class);
            return response != null && "success".equals(response.getStatus()) ? response.getCity()+", "+response.getRegionName()+", "+response.getCountry() : "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }
}