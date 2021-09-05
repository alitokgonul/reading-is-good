package com.getir.readingisgood.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.readingisgood.controller.model.BookOrderDTO;
import com.getir.readingisgood.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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

    private List<BookOrderDTO> createBookOrderDtos(Long bookId, Integer quantity) {
        BookOrderDTO dto = new BookOrderDTO();
        dto.setBookId(bookId);
        dto.setQuantity(quantity);
        return Collections.singletonList(dto);
    }
}