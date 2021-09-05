package com.getir.readingisgood.controller.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonthlyOrderDTO {

    @ApiModelProperty(notes = "Monthly date")
    private LocalDate date;

    @ApiModelProperty(notes = "Total order count")
    private Integer totalOrderCount;

    @ApiModelProperty(notes = "Total book count")
    private Integer totalBookCount;

    @ApiModelProperty(notes = "Total price in the month")
    private BigDecimal totalPrice;
}
