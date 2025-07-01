package com.danny.lansentinel.controller;

import com.danny.lansentinel.scheduler.ScanScheduler;
import com.danny.lansentinel.service.ScanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ScanControllerTest {

    private MockMvc mockMvc;

    private ScanService scanService;
    private ScanScheduler scanScheduler;

    @BeforeEach
    void setUp() {
        scanService = Mockito.mock(ScanService.class);
        scanScheduler = Mockito.mock(ScanScheduler.class);

        ScanController scanController = new ScanController(scanService, scanScheduler);
        mockMvc = MockMvcBuilders.standaloneSetup(scanController).build();
    }

    @Test
    void testCheckScanEnabled() throws Exception {
        when(scanService.isScanEnabled()).thenReturn(true);

        mockMvc.perform(get("/scan/enabled"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testEnableScanTrue() throws Exception {
        when(scanService.setScanEnabled(true)).thenReturn(true);

        mockMvc.perform(get("/scan/enable/true"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testEnableScanFalse() throws Exception {
        when(scanService.setScanEnabled(false)).thenReturn(false);

        mockMvc.perform(get("/scan/enable/false"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void testGetScanInterval() throws Exception {
        when(scanScheduler.getScanIntervalMs()).thenReturn(30000L);

        mockMvc.perform(get("/scan/interval"))
                .andExpect(status().isOk())
                .andExpect(content().string("30000"));
    }

    @Test
    void testSetScanInterval() throws Exception {
        when(scanScheduler.updateScanInterval(60000L)).thenReturn(60000L);

        mockMvc.perform(put("/scan/setinterval")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"interval\":60000}"))
                .andExpect(status().isOk())
                .andExpect(content().string("60000"));
    }
}
