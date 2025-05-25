package com.danny.lansentinel.controller;

import com.danny.lansentinel.service.NetworkScannerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scan")
public class ScanController {

    NetworkScannerService networkScannerService;

    public ScanController(NetworkScannerService networkScannerService) {
        this.networkScannerService = networkScannerService;
    }

    @GetMapping("/enabled")
    public ResponseEntity<Boolean> checkScanEnabled() {
        return ResponseEntity.ok(networkScannerService.isScanEnabled());
    }

    @GetMapping("/enable/{enabled}")
    public ResponseEntity<Boolean> enableScan(@PathVariable Boolean enabled) {
        return ResponseEntity.ok(networkScannerService.setScanEnabled(enabled));
    }

}
