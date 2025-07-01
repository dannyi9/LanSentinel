package com.danny.lansentinel.service;

import com.danny.lansentinel.entity.Device;
import com.danny.lansentinel.repository.DeviceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DeviceService {

    private static final Logger logger = LoggerFactory.getLogger(DeviceService.class);

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
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
            device.setCreatedAt(LocalDateTime.now());
            deviceRepository.save(device);
        }
    }

    public void saveCurrentDevice(Device device, Set<String> seenMacs) {
        if (device != null) {
            saveDevice(device);
            if (device.getMacAddress() != null) {
                seenMacs.add(device.getMacAddress());
            }
        }
    }

    public void markUnseenDevicesAsOffline(Set<String> seenMacs) {
        List<Device> devices = deviceRepository.findAll();
        for (Device device : devices) {
            String mac = device.getMacAddress();
            if (mac != null && !seenMacs.contains(mac) && device.getIsOnline()) {
                logger.info("Removed device:{}", device.getMacAddress());
                device.setIsOnline(false);
                deviceRepository.save(device);
            }
        }
    }

}
