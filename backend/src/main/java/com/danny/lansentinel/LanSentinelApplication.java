package com.danny.lansentinel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LanSentinelApplication {

    public static void main(String[] args) {
        SpringApplication.run(LanSentinelApplication.class, args);
    }

}
