package com.danny.lansentinel;

import com.danny.lansentinel.config.ScanProperties;
import com.danny.lansentinel.service.NetworkScannerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NetworkScanScheduler {

    private final NetworkScannerService networkScannerService;
    private final ScanProperties scanProperties;

    public NetworkScanScheduler(NetworkScannerService networkScannerService, ScanProperties scanProperties) {
        this.networkScannerService = networkScannerService;
        this.scanProperties = scanProperties;
    }

    @Scheduled(fixedDelayString = "#{@scanProperties.scanIntervalMs}")
    public void scheduleNetworkScan() {
        networkScannerService.scanAndSaveDevices(scanProperties.getSubnetToScan());
    }
}
