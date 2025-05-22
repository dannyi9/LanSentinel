package com.danny.lansentinel;

import com.danny.lansentinel.service.NetworkScannerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NetworkScanScheduler {

    private final NetworkScannerService networkScannerService;

    public NetworkScanScheduler(NetworkScannerService networkScannerService) {
        this.networkScannerService = networkScannerService;
    }

    @Scheduled(fixedDelayString = "#{@scanProperties.scanIntervalMs}")
    public void scheduleNetworkScan() {
        networkScannerService.scanAndSaveDevices("192.168.100.0/24");
    }
}
