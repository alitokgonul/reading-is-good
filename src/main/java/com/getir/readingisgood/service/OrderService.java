package com.getir.readingisgood.service;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.getir.readingisgood.controller.model.BookOrderDTO;
import com.getir.readingisgood.controller.model.OrderDTO;
import com.getir.readingisgood.entity.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderService {

    void order(HttpServletRequest req, List<BookOrderDTO> bookOrderList);

    void updateStatus(Long id, OrderStatus orderStatus);

    OrderDTO getById(Long id);

    Page<OrderDTO> listOrders(HttpServletRequest req, Integer page, Integer size);

    List<OrderDTO> filterByDate(LocalDate startDate, LocalDate endDate);
}
