package com.getir.readingisgood.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.getir.readingisgood.controller.model.BookOrderDTO;
import com.getir.readingisgood.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
@Api(tags = { "Order" })
@SwaggerDefinition(tags = { @Tag(name = "Order", description = "Order management") })
public class OrderController {

    private final OrderService orderService;

    @ApiOperation(value = "Order books")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "The new book is successfully ordered."),
                            @ApiResponse(code = 400, message = "Please check your request"),
                            @ApiResponse(code = 401, message = "Authorization error"),
                            @ApiResponse(code = 500, message = "Unexpected server error") })
    @PostMapping
    public ResponseEntity order(HttpServletRequest req, @RequestBody @Valid List<BookOrderDTO> bookOrderList) {
        orderService.order(req, bookOrderList);
        return new ResponseEntity(HttpStatus.OK);
    }
}
