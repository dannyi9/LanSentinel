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

    public Long getId() {
        return this.id;
    }

    public String getHostname() {
        return this.hostname;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getMacAddress() {
        return this.macAddress;
    }

    public String getVendor() {
        return this.vendor;
    }

    public LocalDateTime getLastSeen() {
        return this.lastSeen;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public void setLastSeen(LocalDateTime lastSeen) {
        this.lastSeen = lastSeen;
    }
}
