package com.danny.lansentinel.controller;

import com.danny.lansentinel.entity.Device;
import com.danny.lansentinel.repository.DeviceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/devices/all")
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/devices/{id}")
    public Device getDeviceById(@PathVariable Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @GetMapping("/devices/{id}/trusted")
    public ResponseEntity<Boolean> isTrusted(@PathVariable Long id) {
        return deviceRepository.findById(id)
                .map(device -> ResponseEntity.ok(device.isTrusted()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/devices/{id}/trust")
    public ResponseEntity<Device> setDeviceTrusted(@PathVariable Long id, @RequestParam boolean trusted) {
        return deviceRepository.findById(id).map(device -> {
            device.setTrusted(trusted);
            deviceRepository.save(device);
            return ResponseEntity.ok(device);
        }).orElse(ResponseEntity.notFound().build());
    }

}
