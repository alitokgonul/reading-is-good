package com.getir.readingisgood.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.getir.readingisgood.controller.model.BookOrderDTO;

public interface OrderService {

    void order(HttpServletRequest req, List<BookOrderDTO> bookOrderList);
}
