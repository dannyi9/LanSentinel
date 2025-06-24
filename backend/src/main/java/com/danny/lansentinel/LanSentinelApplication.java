package com.danny.lansentinel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LanSentinelApplication {

    private static final Logger logger = LoggerFactory.getLogger(LanSentinelApplication.class);

    public static void main(String[] args) {
        logger.info("LanSentinel starting...");
        SpringApplication.run(LanSentinelApplication.class, args);
    }

}
