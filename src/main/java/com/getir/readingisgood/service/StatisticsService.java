package com.getir.readingisgood.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.getir.readingisgood.controller.model.MonthlyOrderDTO;

public interface StatisticsService {

    List<MonthlyOrderDTO> listMonthlyOrder(HttpServletRequest req);
}
