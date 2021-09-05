package com.getir.readingisgood.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.getir.readingisgood.controller.model.BookOrderDTO;
import com.getir.readingisgood.controller.model.OrderDTO;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.entity.OrderDetail;
import com.getir.readingisgood.entity.OrderStatus;
import com.getir.readingisgood.exception.CustomException;
import com.getir.readingisgood.repository.OrderDetailRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderDetailRepository orderDetailRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private BookService bookService;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void order() {
        // given
        given(customerService.getCustomerInfo(any())).willReturn(createCustomer());
        given(bookService.getById(anyLong())).willReturn(Optional.of(createBook(3)));

        // when
        orderService.order(mock(HttpServletRequest.class), createBookOrderDtos(1L, 3));

        // then
        verify(bookService).update(anyLong(), anyInt());
        verify(orderDetailRepository).save(any(OrderDetail.class));
    }

    @Test
    void order_book_not_found() {
        // given
        given(bookService.getById(anyLong())).willReturn(Optional.empty());
        List<BookOrderDTO> bookOrderDTOList = createBookOrderDtos(1L, 3);

        try {
            // when
            orderService.order(mock(HttpServletRequest.class), bookOrderDTOList);
            fail("exception not thrown");
        } catch (CustomException ex) {
            // then
            assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertTrue(ex.getMessage().contains("Book with id"));
            verify(bookService, never()).update(anyLong(), anyInt());
            verify(orderDetailRepository, never()).save(any(OrderDetail.class));
        }
    }

    @Test
    void order_insufficient_quantity() {
        // given
        given(bookService.getById(anyLong())).willReturn(Optional.of(createBook(1)));
        List<BookOrderDTO> bookOrderDTOList = createBookOrderDtos(1L, 3);

        try {
            // when
            orderService.order(mock(HttpServletRequest.class), bookOrderDTOList);
            fail("exception not thrown");
        } catch (CustomException ex) {
            // then
            assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertTrue(ex.getMessage().contains("Stock is not sufficient"));
            verify(bookService, never()).update(anyLong(), anyInt());
            verify(orderDetailRepository, never()).save(any(OrderDetail.class));
        }
    }

    @Test
    void updateStatus_success() {
        // given
        given(orderDetailRepository.findById(any())).willReturn(Optional.of(createOrderDetail()));

        // when
        orderService.updateStatus(1L, OrderStatus.DELIVERED);

        // then
        verify(orderDetailRepository).findById(anyLong());
        verify(orderDetailRepository).save(any(OrderDetail.class));
    }

    @Test
    void updateStatus_fail() {
        // given
        given(orderDetailRepository.findById(any())).willReturn(Optional.empty());

        try {
            // when
            orderService.updateStatus(1L, OrderStatus.DELIVERED);
            fail("exception not thrown");
        } catch (CustomException ex) {
            // then
            assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertTrue(ex.getMessage().contains("Order with id"));
            verify(orderDetailRepository, never()).save(any());
        }
    }

    @Test
    void getById_success() {
        // given
        final OrderDetail orderDetail = createOrderDetail();
        given(orderDetailRepository.findById(any())).willReturn(Optional.of(orderDetail));

        // when
        OrderDTO dto = orderService.getById(1L);

        // then
        assertEquals(dto.getId(), orderDetail.getId());
        assertEquals(dto.getOrderStatus(), orderDetail.getOrderStatus());
    }

    @Test
    void getById_fail() {
        // given
        given(orderDetailRepository.findById(any())).willReturn(Optional.empty());

        try {
            // when
            orderService.getById(1L);
            fail("exception not thrown");
        } catch (CustomException ex) {
            // then
            assertEquals(ex.getHttpStatus(), HttpStatus.BAD_REQUEST);
            assertTrue(ex.getMessage().contains("Order with id"));
        }
    }

    @Test
    void listOrders() {
        // given
        given(customerService.getCustomerInfo(any())).willReturn(createCustomer());
        given(orderDetailRepository.filterOrders(any(), any())).willReturn(createOrderDetailPage());

        // when
        Page<OrderDTO> dto = orderService.listOrders(mock(HttpServletRequest.class), 0, 2);

        // then
        assertNotNull(dto.getContent());
        assertEquals(dto.getContent().get(0).getId(), 1L);
        assertEquals(dto.getContent().get(0).getOrderStatus(), OrderStatus.PROCESSING);
    }

    @Test
    void filterByDate() {
        // given
        given(orderDetailRepository.findAllByCreatedDateTimeAfterAndCreatedDateTimeBefore(any(), any())).willReturn(
            Arrays.asList(createOrderDetail()));

        // when
        List<OrderDTO> dto = orderService.filterByDate(LocalDate.of(2020, 01, 02), LocalDate.of(2024, 01, 01));

        // then
        assertNotNull(dto);
        assertEquals(dto.get(0).getId(), 1L);
        assertEquals(dto.get(0).getOrderStatus(), OrderStatus.PROCESSING);
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setPhone("55555555555");
        customer.setUsername("test");
        customer.setPassword("test");

        return customer;
    }

    private Book createBook(int quantity) {
        Book book = new Book();
        book.setId(1L);
        book.setName("book name");
        book.setQuantity(quantity);

        return book;
    }

    private List<BookOrderDTO> createBookOrderDtos(Long bookId, Integer quantity) {
        BookOrderDTO dto = new BookOrderDTO();
        dto.setBookId(bookId);
        dto.setQuantity(quantity);
        return Collections.singletonList(dto);
    }

    private OrderDetail createOrderDetail() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setId(1L);
        orderDetail.setOrderStatus(OrderStatus.PROCESSING);
        return orderDetail;
    }

    private Page<OrderDetail> createOrderDetailPage() {

        return new PageImpl<>(Collections.singletonList(createOrderDetail()));
    }
}