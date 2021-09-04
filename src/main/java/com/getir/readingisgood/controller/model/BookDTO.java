package com.getir.readingisgood.controller.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {

    @ApiModelProperty(notes = "Id")
    private Long id;

    @ApiModelProperty(notes = "Customer name", required = true, example = "Flowers for Algernon")
    @NotBlank(message = "Name is mandatory")
    private String name;

    @ApiModelProperty(notes = "Customer name", required = true, example = "1959-12-04")
    private LocalDate publishDate;

    @ApiModelProperty(notes = "Price", required = true, example = "12.4")
    @Positive(message = "Book price should be positive")
    @NotNull(message = "Price is mandatory")
    private BigDecimal price;

    @ApiModelProperty(notes = "Book quantity", required = true, example = "3")
    @Positive(message = "Book quantity should be positive")
    @NotNull(message = "Quantity is mandatory")
    private Integer quantity;

    @ApiModelProperty(notes = "Author information", required = true)
    @NotNull(message = "Author information is mandatory")
    @Valid
    private AuthorDTO author;
}
