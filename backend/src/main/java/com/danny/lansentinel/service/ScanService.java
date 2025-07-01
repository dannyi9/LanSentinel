package com.danny.lansentinel.service;

import com.danny.lansentinel.entity.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
public class ScanService {

    private static final Logger logger = LoggerFactory.getLogger(ScanService.class);

    private final DeviceService deviceService;

    private boolean scanEnabled = true;

    public ScanService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void scanAndSaveDevices(String subnet) {
        if (!scanEnabled)
            return;

        System.out.println("Starting network scan for subnet: " + subnet);

        Set<String> seenMacs = new HashSet<>();

        try {
            Process process = new ProcessBuilder("nmap", "-sn", subnet)
                    .redirectErrorStream(true)
                    .start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                Device currentDevice = null;

                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("Nmap scan report for")) {
                        deviceService.saveCurrentDevice(currentDevice, seenMacs);
                        currentDevice = parseScanReportLine(line);
                    } else if (line.trim().startsWith("MAC Address:") && currentDevice != null) {
                        parseMacAddressLine(line, currentDevice);
                    }
                }

                deviceService.saveCurrentDevice(currentDevice, seenMacs);
            }

            // Mark unseen devices as offline
            deviceService.markUnseenDevicesAsOffline(seenMacs);

            System.out.println("Finished network scan for subnet: " + subnet);

        } catch (IOException e) {
            logger.error("Error during network scan: ", e);
        }
    }

    public Boolean isScanEnabled() {
        return scanEnabled;
    }

    public Boolean setScanEnabled(Boolean enable) {
        scanEnabled = enable;
        logger.info("Network scan status set to: {}", scanEnabled ? "ENABLED" : "DISABLED");
        return scanEnabled;
    }

    private Device parseScanReportLine(String line) {
        Device device = new Device();

        String data = line.substring("Nmap scan report for ".length()).trim();
        if (data.contains("(") && data.contains(")")) {
            String hostname = data.substring(0, data.indexOf('(')).trim();
            String ip = data.substring(data.indexOf('(') + 1, data.indexOf(')')).trim();
            device.setHostname(hostname);
            device.setIpAddress(ip);
        } else {
            device.setIpAddress(data);
        }

        device.setIsOnline(true);
        device.setLastSeen(LocalDateTime.now());

        return device;
    }

    private void parseMacAddressLine(String line, Device device) {
        try {
            String[] parts = line.split("MAC Address:")[1].trim().split(" ", 2);
            String mac = parts[0].trim();
            device.setMacAddress(mac);

            if (parts.length > 1 && parts[1].contains("(") && parts[1].contains(")")) {
                String vendor = parts[1].substring(parts[1].indexOf('(') + 1, parts[1].indexOf(')')).trim();
                device.setVendor(vendor);
            }
        } catch (Exception e) {
            logger.error("Error during MAC address parsing: ", e);
        }
    }

}
