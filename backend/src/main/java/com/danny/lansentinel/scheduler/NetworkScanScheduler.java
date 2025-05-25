package com.danny.lansentinel.scheduler;

import com.danny.lansentinel.config.ScanProperties;
import com.danny.lansentinel.service.NetworkScannerService;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Component
public class NetworkScanScheduler {

    private final NetworkScannerService networkScannerService;
    private final ScanProperties scanProperties;

    private final ThreadPoolTaskScheduler taskScheduler;
    private ScheduledFuture<?> scheduledFuture;

    private long scanIntervalMs;

    public NetworkScanScheduler(NetworkScannerService networkScannerService,
                                ScanProperties scanProperties,
                                ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.networkScannerService = networkScannerService;
        this.scanProperties = scanProperties;
        this.taskScheduler = threadPoolTaskScheduler;
        this.scanIntervalMs = scanProperties.getScanIntervalMs();
        scheduleTask();
    }

    private void scheduleTask() {
        System.out.println("Scheduling network scan task");
        scheduledFuture = taskScheduler.scheduleWithFixedDelay(
                () -> networkScannerService.scanAndSaveDevices(scanProperties.getSubnetToScan()),
                Duration.ofMillis(scanIntervalMs)
        );
    }

    public void updateScanInterval(long newIntervalMs) {
        this.scanIntervalMs = newIntervalMs;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        scheduleTask();
    }

    public long getScanIntervalMs() {
        return scanIntervalMs;
    }
}
