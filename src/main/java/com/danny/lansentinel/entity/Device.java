package com.danny.lansentinel.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostname;
    private String ipAddress;
    private String macAddress;
    private String vendor;

    private String operatingSystem;

    private Integer openPortCount;
    private List<Integer> openPorts;

    private Boolean isOnline;
    private LocalDateTime lastSeen;

    private Boolean isTrusted;

    private String notes;
}
