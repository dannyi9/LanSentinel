package com.danny.lansentinel.controller;

import com.danny.lansentinel.entity.Device;
import com.danny.lansentinel.repository.DeviceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/all")
    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }

    @GetMapping("/{id}")
    public Device getDeviceById(@PathVariable Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @GetMapping("/{id}/trusted")
    public ResponseEntity<Boolean> isTrusted(@PathVariable Long id) {
        return deviceRepository.findById(id)
                .map(device -> ResponseEntity.ok(device.getIsTrusted()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/trust")
    public ResponseEntity<Device> setDeviceTrusted(@PathVariable Long id, @RequestParam boolean trusted) {
        return deviceRepository.findById(id).map(device -> {
            device.setIsTrusted(trusted);
            deviceRepository.save(device);
            return ResponseEntity.ok(device);
        }).orElse(ResponseEntity.notFound().build());
    }

}
