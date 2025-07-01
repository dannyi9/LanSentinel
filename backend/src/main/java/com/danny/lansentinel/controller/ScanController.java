package com.danny.lansentinel.controller;

import com.danny.lansentinel.scheduler.ScanScheduler;
import com.danny.lansentinel.service.ScanService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scan")
public class ScanController {

    private final ScanService scanService;
    private final ScanScheduler scanScheduler;

    public ScanController(ScanService scanService,
                          ScanScheduler scanScheduler) {
        this.scanService = scanService;
        this.scanScheduler = scanScheduler;
    }

    public static class IntervalRequest {
        private Long interval;

        public Long getInterval() {
            return interval;
        }

        public void setInterval(Long interval) {
            this.interval = interval;
        }
    }

    @GetMapping("/enabled")
    public ResponseEntity<Boolean> checkScanEnabled() {
        return ResponseEntity.ok(scanService.isScanEnabled());
    }

    @GetMapping("/enable/{enabled}")
    public ResponseEntity<Boolean> enableScan(@PathVariable Boolean enabled) {
        return ResponseEntity.ok(scanService.setScanEnabled(enabled));
    }

    @GetMapping("/interval")
    public ResponseEntity<Long> getScanInterval() {
        return ResponseEntity.ok(scanScheduler.getScanIntervalMs());
    }

    @PutMapping("/setinterval")
    public ResponseEntity<Long> setScanInterval(@RequestBody IntervalRequest req) {
        return ResponseEntity.ok(scanScheduler.updateScanInterval(req.getInterval()));
    }

}
