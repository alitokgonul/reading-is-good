package com.getir.readingisgood.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.readingisgood.controller.model.BookOrderDTO;
import com.getir.readingisgood.controller.model.OrderDTO;
import com.getir.readingisgood.entity.OrderStatus;
import com.getir.readingisgood.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private static final String ORDER_ENDPOINT = "/api/v1/orders";

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new OrderController(orderService)).build();
    }

    @Test
    void order_success() throws Exception {
        // given
        List<BookOrderDTO> request = createBookOrderDtos(1L, 2);
        String createRequestStr = objectMapper.writeValueAsString(request);

        // when / then
        mockMvc.perform(post(ORDER_ENDPOINT).contentType(MediaType.APPLICATION_JSON).content(createRequestStr))
               .andExpect(status().isOk())
               .andReturn();
    }

    @Test
    void orderStatusUpdate_success() throws Exception {
        // given
        List<BookOrderDTO> request = createBookOrderDtos(1L, 2);
        String createRequestStr = objectMapper.writeValueAsString(request);

        // when / then
        mockMvc.perform(put(ORDER_ENDPOINT
                            + "/status-update/1?orderStatus=DELIVERED").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn();
    }

    @Test
    void orderStatusUpdate_without_orderStatus() throws Exception {
        // given
        List<BookOrderDTO> request = createBookOrderDtos(1L, 2);
        String createRequestStr = objectMapper.writeValueAsString(request);

        // when / then
        mockMvc.perform(put(ORDER_ENDPOINT + "/status-update/1").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void getOrderById_success() throws Exception {
        // given
        given(orderService.getById(anyLong())).willReturn(createOrderDTO());

        // when
        MvcResult mvcResult = mockMvc.perform(get(ORDER_ENDPOINT + "/1").contentType(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();

        // then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        OrderDTO dto = objectMapper.readValue(contentAsString, OrderDTO.class);

        assertEquals(dto.getId(), 1L);
        assertEquals(dto.getOrderStatus(), OrderStatus.PROCESSING);
    }

    @Test
    void filterByDate_success() throws Exception {
        // given
        given(orderService.filterByDate(any(), any())).willReturn(Arrays.asList(createOrderDTO()));

        // when / then
        mockMvc.perform(get(ORDER_ENDPOINT + "/filter-date?endDate=2020-10-31&startDate=2021-01-01").contentType(
            MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn();
    }

    private List<BookOrderDTO> createBookOrderDtos(Long bookId, Integer quantity) {
        BookOrderDTO dto = new BookOrderDTO();
        dto.setBookId(bookId);
        dto.setQuantity(quantity);
        return Collections.singletonList(dto);
    }

    private OrderDTO createOrderDTO() {
        OrderDTO dto = new OrderDTO();
        dto.setId(1L);
        dto.setOrderStatus(OrderStatus.PROCESSING);
        return dto;
    }
}