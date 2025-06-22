package com.danny.lansentinel.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.danny.lansentinel.entity.Device;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeviceEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final String topic = "devices";

    public DeviceEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
        this.objectMapper.registerModule(new JavaTimeModule());
    }
    public void publishDevice(Device device) {
        try {
            String message = objectMapper.writeValueAsString(device);
            kafkaTemplate.send(topic, device.getMacAddress(), message);
        } catch (Exception e) {
            System.err.println("Failed to publish Kafka message: " + e.getMessage());
        }
    }
}
