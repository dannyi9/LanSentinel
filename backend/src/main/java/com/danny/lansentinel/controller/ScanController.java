package com.danny.lansentinel.controller;

import com.danny.lansentinel.service.NetworkScannerService;

import com.danny.lansentinel.entity.Device;
import com.danny.lansentinel.repository.DeviceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScanController {

    NetworkScannerService networkScannerService;

    public ScanController(NetworkScannerService networkScannerService) {
        this.networkScannerService = networkScannerService;
    }

    @GetMapping("/scan/enabled")
    public ResponseEntity<Boolean> checkScanEnabled() {
        return ResponseEntity.ok(networkScannerService.isScanEnabled());
    }

    @GetMapping("/scan/enable/{enabled}")
    public ResponseEntity<Boolean> enableScan(@PathVariable Boolean enabled) {
        return ResponseEntity.ok(networkScannerService.setScanEnabled(enabled));
    }

}
