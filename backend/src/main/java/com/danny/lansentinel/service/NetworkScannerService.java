package com.danny.lansentinel.service;

import com.danny.lansentinel.entity.Device;
import com.danny.lansentinel.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class NetworkScannerService {

    private static final Logger logger = LoggerFactory.getLogger(NetworkScannerService.class);

    private final DeviceRepository deviceRepository;

    private boolean scanEnabled = true;

    public NetworkScannerService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public String formatDateTime(LocalDateTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return time.format(formatter);
    }

    public void saveDevice(Device device) {
        Optional<Device> optionalExistingDevice = deviceRepository.findByMacAddress(device.getMacAddress());
        if (optionalExistingDevice.isPresent()) {
            Device existing = optionalExistingDevice.get();
            existing.setHostname(device.getHostname());
            existing.setIpAddress(device.getIpAddress());
            existing.setVendor(device.getVendor());
            existing.setIsOnline(true);
            existing.setLastSeen(LocalDateTime.now());
            deviceRepository.save(existing);
        } else {
            logger.info("New device found: {}", device.getIpAddress() + ":" + device.getHostname());
            device.setIsOnline(true);
            device.setLastSeen(LocalDateTime.now());
            deviceRepository.save(device);
        }
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
                        if (currentDevice != null) {
                            saveDevice(currentDevice);
                            if (currentDevice.getMacAddress() != null) {
                                seenMacs.add(currentDevice.getMacAddress());
                            }
                        }
                        currentDevice = parseScanReportLine(line);
                    } else if (line.trim().startsWith("MAC Address:") && currentDevice != null) {
                        parseMacAddressLine(line, currentDevice);
                    }
                }

                if (currentDevice != null) {
                    saveDevice(currentDevice);
                    if (currentDevice.getMacAddress() != null) {
                        seenMacs.add(currentDevice.getMacAddress());
                    }
                }

            }

            // Mark unseen devices as offline
            List<Device> devices = deviceRepository.findAll();
            for (Device device : devices) {
                String mac = device.getMacAddress();
                if (mac != null && !seenMacs.contains(mac) && device.getIsOnline()) {
                    logger.info("Removed device:{}", device.getMacAddress());
                    device.setIsOnline(false);
                    deviceRepository.save(device);
                }
            }

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

    private void printAllDevices() {
        List<Device> allDevices = deviceRepository.findAll();
        System.out.println("=== All Saved Devices ===");
        allDevices.forEach(device -> System.out.printf(
                "IP: %s, Hostname: %s, MAC: %s, Vendor: %s, Last Seen: %s, Online: %s%n",
                defaultIfNull(device.getIpAddress()),
                defaultIfNull(device.getHostname()),
                defaultIfNull(device.getMacAddress()),
                defaultIfNull(device.getVendor()),
                formatDateTime(device.getLastSeen()),
                defaultIfNull(String.valueOf(device.getIsOnline()))
        ));
    }

    private String defaultIfNull(String value) {
        return value != null ? value : "n/a";
    }
}
