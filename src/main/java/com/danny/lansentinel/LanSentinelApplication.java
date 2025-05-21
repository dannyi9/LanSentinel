package com.danny.lansentinel;

import com.danny.lansentinel.service.NetworkScannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LanSentinelApplication implements CommandLineRunner {

    @Autowired
    private NetworkScannerService networkScannerService;

    public static void main(String[] args) {
        SpringApplication.run(LanSentinelApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        networkScannerService.scanAndSaveDevices("192.168.100.0/24");
    }

}
