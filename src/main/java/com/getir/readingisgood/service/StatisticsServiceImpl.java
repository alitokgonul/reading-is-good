package com.getir.readingisgood.service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.getir.readingisgood.controller.model.MonthlyOrderDTO;
import com.getir.readingisgood.entity.Book;
import com.getir.readingisgood.entity.OrderDetail;
import com.getir.readingisgood.entity.OrderItem;
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

        Map<Month, List<OrderDetail>> entries =
            orderDetails.stream().collect(Collectors.groupingBy(item -> item.getCreatedDateTime().getMonth()));

        entries.entrySet().stream().forEach(entry -> {
            Integer totalQuantity = 0;
            BigDecimal totalMonthPrice = new BigDecimal(0);
            MonthlyOrderDTO dto = new MonthlyOrderDTO();
            Set<Long> bookIds = new HashSet<Long>();
            // set month
            dto.setMonth(entry.getKey());
            for (OrderDetail orderDetail : entry.getValue()) {
                // find monthly total order
                int orderQuantity = orderDetail.getOrderItemList().stream().mapToInt(OrderItem::getQuantity).sum();
                totalQuantity = totalQuantity + orderQuantity;
                BigDecimal orderPrice = new BigDecimal(0);

                for (OrderItem orderItem : orderDetail.getOrderItemList()) {
                    Book book = orderItem.getBook();
                    orderPrice = orderPrice.add(book.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())));
                    bookIds.add(book.getId());
                }
                totalMonthPrice = totalMonthPrice.add(orderPrice);
            }
            dto.setTotalOrderCount(totalQuantity);
            dto.setTotalPrice(totalMonthPrice);
            dto.setTotalBookCount(bookIds.size());
            monthlyOrderDTOList.add(dto);
        });

        return monthlyOrderDTOList;
    }
}
