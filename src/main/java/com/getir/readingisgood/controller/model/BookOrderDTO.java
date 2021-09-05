package com.getir.readingisgood.controller.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookOrderDTO {

    @ApiModelProperty(notes = "Book id", required = true, example = "1")
    @NotNull(message = "Book id is mandatory")
    private Long bookId;

    @ApiModelProperty(notes = "Book order quantity", required = true, example = "3")
    @Positive(message = "Book quantity should be positive")
    @NotNull(message = "Quantity is mandatory")
    private Integer quantity;
}
