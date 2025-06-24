package com.danny.lansentinel.scheduler;

import com.danny.lansentinel.config.ScanProperties;
import com.danny.lansentinel.service.NetworkScannerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ScheduledFuture;

@Component
public class NetworkScanScheduler {

    private static final Logger logger = LoggerFactory.getLogger(NetworkScanScheduler.class);

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
        scheduleScanTask();
    }

    private void runNetworkScan(String subnet) {
        networkScannerService.scanAndSaveDevices(subnet);
    }

    private void scheduleScanTask() {
        String subnetToScan = scanProperties.getSubnetToScan();
        if (subnetToScan == null || subnetToScan.isEmpty()) {
            logger.warn("No subnet to configured, skipping scan");
            return;
        }

        System.out.println("Scheduling network scan task");
        scheduledFuture = taskScheduler.scheduleWithFixedDelay(
                    () -> runNetworkScan(subnetToScan),
                Duration.ofMillis(scanIntervalMs)
        );
    }

    public synchronized long updateScanInterval(long newIntervalMs) {
        if (this.scanIntervalMs == newIntervalMs) {
            logger.info("Interval unchanged, no reschedule needed");
            return this.scanIntervalMs;
        }

        logger.info("Updating network scan interval to {}", newIntervalMs);
        this.scanIntervalMs = newIntervalMs;

        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
        }
        scheduleScanTask();
        return this.scanIntervalMs;
    }

    public long getScanIntervalMs() {
        return scanIntervalMs;
    }
}
