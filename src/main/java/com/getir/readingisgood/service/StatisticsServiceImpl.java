package com.getir.readingisgood.service;

import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.getir.readingisgood.controller.model.MonthlyOrderDTO;
import com.getir.readingisgood.entity.OrderDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final OrderService orderService;

    @Override
    @Transactional
    public List<MonthlyOrderDTO> listMonthlyOrder(HttpServletRequest req) {
        List<MonthlyOrderDTO> monthlyOrderDTOList = new ArrayList<>();
        List<OrderDetail> orderDetails = orderService.listUserAllOrders(req);
        Integer totalBookCount = 0;

        Map<Month, List<OrderDetail>> entries =
            orderDetails.stream().collect(Collectors.groupingBy(item -> item.getCreatedDateTime().getMonth()));

        entries.entrySet().stream().forEach(entry -> {
            entry.getValue().forEach(orderDetail -> {
//                orderDetail.getOrderItemList().
            });
        });

        return null;
    }
}
