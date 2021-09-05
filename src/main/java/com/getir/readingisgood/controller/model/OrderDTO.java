package com.getir.readingisgood.controller.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.entity.OrderItem;
import com.getir.readingisgood.entity.OrderStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {

    @ApiModelProperty(notes = "Id")
    private Long id;

    @ApiModelProperty(notes = "Order status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ApiModelProperty(notes = "Customer info")
    private Customer customer;

    @ApiModelProperty(notes = "Ordered item list")
    private List<OrderItem> orderItemList;

    @ApiModelProperty(notes = "Order date")
    private LocalDateTime createdDateTime;

}
