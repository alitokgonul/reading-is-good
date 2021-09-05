package com.getir.readingisgood.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.getir.readingisgood.controller.model.BookOrderDTO;
import com.getir.readingisgood.entity.OrderStatus;

public interface OrderService {

    void order(HttpServletRequest req, List<BookOrderDTO> bookOrderList);

    void updateStatus(Long id, OrderStatus orderStatus);
}
