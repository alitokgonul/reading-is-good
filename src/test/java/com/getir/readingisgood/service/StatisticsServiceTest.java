package com.getir.readingisgood.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.getir.readingisgood.controller.model.MonthlyOrderDTO;
import com.getir.readingisgood.entity.Author;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.entity.OrderDetail;
import com.getir.readingisgood.entity.OrderItem;
import com.getir.readingisgood.entity.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatisticsServiceTest {

    @Mock
    private OrderServiceImpl orderService;

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Test
    void listMonthlyOrder() {
        // given
        given(orderService.listUserAllOrders(any())).willReturn(createOrderDetails());

        // when
        List<MonthlyOrderDTO> dtos = statisticsService.listMonthlyOrder(any());

        // then
        assertNotNull(dtos);
        assertEquals(dtos.size(), 1);
        assertEquals(dtos.get(0).getTotalOrderCount(), 2);
        assertEquals(dtos.get(0).getTotalBookCount(), 1);
        assertEquals(dtos.get(0).getTotalPrice(), new BigDecimal(24));
    }

    private List<OrderDetail> createOrderDetails() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1L);
        orderDetail.setOrderStatus(OrderStatus.PROCESSING);
        orderDetail.setCreatedDateTime(LocalDateTime.now());
        orderDetail.setOrderItemList(Arrays.asList(createOrderItem()));
        orderDetails.add(orderDetail);
        return orderDetails;
    }

    private OrderItem createOrderItem() {
        OrderItem item = new OrderItem();
        item.setId(1L);
        item.setQuantity(2);
        item.setBook(createBook());
        return item;
    }

    private Book createBook() {
        Book book = new Book();
        book.setId(1L);
        book.setName("book name");
        book.setPrice(new BigDecimal(12));
        return book;
    }
}