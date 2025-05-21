package com.danny.lansentinel.service;

import com.danny.lansentinel.entity.Device;
import com.danny.lansentinel.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NetworkScannerService {

    @Autowired
    private DeviceRepository deviceRepository;

    public void scanAndSaveDevices(String subnet) {
        try {
            Process process = new ProcessBuilder("nmap", "-sn", subnet)
                    .redirectErrorStream(true)
                    .start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            Device currentDevice = null;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Nmap scan report for")) {
                    if (currentDevice != null) {
                        deviceRepository.save(currentDevice);
                    }

                    currentDevice = new Device();
                    String[] parts = line.split(" ");
                    String ipOrHost = parts[parts.length - 1];
                    currentDevice.setIpAddress(ipOrHost);
                    currentDevice.setHostname(ipOrHost.contains("(") ? ipOrHost.split("\\(")[0] : null);
                    currentDevice.setIsOnline(true);
                    currentDevice.setLastSeen(LocalDateTime.now());
                } else if (line.trim().startsWith("MAC Address:")) {
                    String[] macParts = line.split(" ");
                    currentDevice.setMacAddress(macParts[2]);
                    currentDevice.setVendor(line.substring(line.indexOf("(", line.indexOf(")") + 1) + 1).replace(")", ""));
                }
            }

            if (currentDevice != null) {
                deviceRepository.save(currentDevice);
            }

            List<Device> allDevices = deviceRepository.findAll();
            System.out.println("=== All Saved Devices ===");
            allDevices.forEach(device -> {
                System.out.println("IP: " + device.getIpAddress() +
                        ", Hostname: " + device.getHostname() +
                        ", MAC: " + device.getMacAddress() +
                        ", Vendor: " + device.getVendor() +
                        ", Last Seen: " + device.getLastSeen());
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
