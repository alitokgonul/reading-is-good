package com.getir.readingisgood.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import com.getir.readingisgood.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class StatisticsControllerTest {

    private static final String STATISTICS_ENDPOINT = "/api/v1/statistics";

    private MockMvc mockMvc;

    @Mock
    private StatisticsService statisticsService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new StatisticsController(statisticsService)).build();
    }

    @Test
    void listMonthlyOrder_success() throws Exception {
        // given
        given(statisticsService.listMonthlyOrder(any())).willReturn(Arrays.asList());

        // when
        mockMvc.perform(get(STATISTICS_ENDPOINT + "/customer-monthly-order").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn();
    }

}