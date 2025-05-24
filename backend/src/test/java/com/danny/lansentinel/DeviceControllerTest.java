package com.danny.lansentinel;

import com.danny.lansentinel.controller.DeviceController;
import com.danny.lansentinel.entity.Device;
import com.danny.lansentinel.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeviceController.class)
@Import(DeviceControllerTest.Config.class)
class DeviceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DeviceRepository mockRepo;

    private Device device1;
    private Device device2;

    @BeforeEach
    void setUp() {
        device1 = new Device();
        device1.setHostname("Device1");
        device1.setIpAddress("192.168.0.2");
        device1.setMacAddress("00:11:22:33:44:55");
        device1.setVendor("Tuna");

        device2 = new Device();
        device2.setHostname("Device2");
        device2.setIpAddress("192.168.0.3");
        device2.setMacAddress("66:77:88:99:AA:BB");
        device2.setVendor("Salmon");

        when(mockRepo.findAll()).thenReturn(Arrays.asList(device1, device2));
        when(mockRepo.findById(1L)).thenReturn(Optional.of(device1));
        when(mockRepo.findById(999L)).thenReturn(Optional.empty());
    }

    @Test
    void getAllDevices_shouldReturnDevices() throws Exception {
        mockMvc.perform(get("/devices/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].hostname", is("Device1")))
                .andExpect(jsonPath("$[1].macAddress", is("66:77:88:99:AA:BB")));
    }

    @Test
    void getDeviceById_shouldReturnDevice() throws Exception {
        mockMvc.perform(get("/devices/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ipAddress", is("192.168.0.2")));
    }

    @Test
    void getDeviceById_shouldReturnNull() throws Exception {
        mockMvc.perform(get("/devices/1337"))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

    @TestConfiguration
    static class Config {

        @Bean
        @Primary
        public DeviceRepository deviceRepository() {
            return Mockito.mock(DeviceRepository.class);
        }
    }
}
