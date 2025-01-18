package com.example.EmployeeRecordsManagementSystem.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Builder
public class AuditLog{
    @Id
    private String id;
    @ManyToOne
    private User user;
    private LocalDateTime date;
    private String action;
    private String details;
    private String ipAddress;
    private String address;

}
