package com.danny.lansentinel.controller;

import com.danny.lansentinel.scheduler.NetworkScanScheduler;
import com.danny.lansentinel.service.NetworkScannerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scan")
public class ScanController {

    NetworkScannerService networkScannerService;
    NetworkScanScheduler networkScanScheduler;

    public ScanController(NetworkScannerService networkScannerService,
                          NetworkScanScheduler networkScanScheduler) {
        this.networkScannerService = networkScannerService;
        this.networkScanScheduler = networkScanScheduler;
    }

    @GetMapping("/enabled")
    public ResponseEntity<Boolean> checkScanEnabled() {
        return ResponseEntity.ok(networkScannerService.isScanEnabled());
    }

    @GetMapping("/enable/{enabled}")
    public ResponseEntity<Boolean> enableScan(@PathVariable Boolean enabled) {
        return ResponseEntity.ok(networkScannerService.setScanEnabled(enabled));
    }

    @GetMapping("/interval")
    public ResponseEntity<Long> getScanInterval() {
        return ResponseEntity.ok(networkScanScheduler.getScanIntervalMs());
    }

}
