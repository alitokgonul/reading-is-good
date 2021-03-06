package com.getir.readingisgood.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.getir.readingisgood.controller.model.BookOrderDTO;
import com.getir.readingisgood.controller.model.OrderDTO;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.entity.OrderDetail;
import com.getir.readingisgood.entity.OrderItem;
import com.getir.readingisgood.entity.OrderStatus;
import com.getir.readingisgood.exception.CustomException;
import com.getir.readingisgood.repository.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final CustomerService customerService;
    private final BookService bookService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void order(HttpServletRequest req, final List<BookOrderDTO> bookOrderList) {
        List<OrderItem> orderInfoList = new ArrayList<>();
        final Customer customer = customerService.getCustomerInfo(req);
        bookOrderList.forEach(orderedBook -> {
            log.info("check the book with id {}", orderedBook.getBookId());
            Book book = bookService.getById(orderedBook.getBookId())
                                   .orElseThrow(() -> new CustomException("Book with id - "
                                                                          + orderedBook.getBookId()
                                                                          + ", could not be found in db.",
                                                                          HttpStatus.BAD_REQUEST));
            if (orderedBook.getQuantity() > book.getQuantity()) {
                throw new CustomException("Stock is not sufficient for the book - " + book.getName(),
                                          HttpStatus.BAD_REQUEST);
            }
            // valid bookId and ordered book quantity is sufficient
            final int restQuantity = book.getQuantity() - orderedBook.getQuantity();
            OrderItem orderInfo = new OrderItem();
            orderInfo.setQuantity(orderedBook.getQuantity());
            orderInfo.setBook(book);
            orderInfoList.add(orderInfo);
            bookService.update(book.getId(), restQuantity);
            log.info("The quantity of book with id - {} is successfully updated.", book.getId());
        });
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderStatus(OrderStatus.PROCESSING);
        orderDetail.setCustomer(customer);
        orderDetail.setOrderItemList(orderInfoList);
        orderDetailRepository.save(orderDetail);
    }

    @Override
    @Transactional
    public void updateStatus(final Long id, final OrderStatus orderStatus) {
        OrderDetail order = orderDetailRepository.findById(id)
                                                 .orElseThrow(() -> new CustomException("Order with id - "
                                                                                        + id
                                                                                        + ", could not be found in db.",
                                                                                        HttpStatus.BAD_REQUEST));
        order.setOrderStatus(orderStatus);
        orderDetailRepository.save(order);
    }

    @Override
    @Transactional
    public OrderDTO getById(final Long id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                                                       .orElseThrow(() -> new CustomException("Order with id - "
                                                                                              + id
                                                                                              + ", could not be found in db.",
                                                                                              HttpStatus.BAD_REQUEST));

        return modelMapper.map(orderDetail, OrderDTO.class);
    }

    @Override
    @Transactional
    public Page<OrderDTO> listOrders(HttpServletRequest req, final Integer page, final Integer size) {
        final Customer customer = customerService.getCustomerInfo(req);
        Pageable pageable = PageRequest.of(page, size);

        Page<OrderDetail> orderDetails = orderDetailRepository.filterOrders(customer.getId(), pageable);
        return orderDetails.map(this::convertOrderDTO);
    }

    @Override
    @Transactional
    public List<OrderDTO> filterByDate(final LocalDate startDate, final LocalDate endDate) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByCreatedDateTimeAfterAndCreatedDateTimeBefore(
            startDate.atTime(LocalTime.MIN),
            endDate.atTime(LocalTime.MAX));

        return orderDetails.stream().map(order -> modelMapper.map(order, OrderDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<OrderDetail> listUserAllOrders(HttpServletRequest req) {
        final Customer customer = customerService.getCustomerInfo(req);

        List<OrderDetail> orderDetails = orderDetailRepository.findAllByCustomer_Id(customer.getId());
        return orderDetails;
    }

    private OrderDTO convertOrderDTO(OrderDetail orderDetail) {
        return modelMapper.map(orderDetail, OrderDTO.class);
    }
}
