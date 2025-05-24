package com.danny.lansentinel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "lansentinel")
public class ScanProperties {
    private long scanIntervalMs;
    private String subnetToScan;

    public long getScanIntervalMs() {
        return scanIntervalMs;
    }

    public String getSubnetToScan() {
        return subnetToScan;
    }

    public void setScanIntervalMs(long scanIntervalMs) {
        this.scanIntervalMs = scanIntervalMs;
    }

    public void setSubnetToScan(String subnetToScan) {
        this.subnetToScan = subnetToScan;
    }
}
