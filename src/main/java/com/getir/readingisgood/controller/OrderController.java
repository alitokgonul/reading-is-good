package com.getir.readingisgood.controller;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.getir.readingisgood.controller.model.BookOrderDTO;
import com.getir.readingisgood.controller.model.OrderDTO;
import com.getir.readingisgood.entity.OrderStatus;
import com.getir.readingisgood.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<Void> order(HttpServletRequest req, @RequestBody @Valid List<BookOrderDTO> bookOrderList) {
        orderService.order(req, bookOrderList);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update order status")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "The status of the order is successfully updated."),
                            @ApiResponse(code = 400, message = "Please check your request"),
                            @ApiResponse(code = 401, message = "Authorization error"),
                            @ApiResponse(code = 500, message = "Unexpected server error") })
    @PutMapping("status-update/{id}")
    public ResponseEntity<Void> orderStatusUpdate(@PathVariable Long id, @RequestParam OrderStatus orderStatus) {
        orderService.updateStatus(id, orderStatus);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Get order detail by id")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Order detail is successfully retrieved."),
                            @ApiResponse(code = 400, message = "Please check your request"),
                            @ApiResponse(code = 401, message = "Authorization error"),
                            @ApiResponse(code = 500, message = "Unexpected server error") })
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {

        return ResponseEntity.ok(orderService.getById(id));
    }

    @ApiOperation(value = "List orders by date interval")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Order details are successfully retrieved."),
                            @ApiResponse(code = 400, message = "Please check your request"),
                            @ApiResponse(code = 401, message = "Authorization error"),
                            @ApiResponse(code = 500, message = "Unexpected server error") })
    @GetMapping("/filter-date")
    public ResponseEntity<List<OrderDTO>> filterByDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                       @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        return ResponseEntity.ok(orderService.filterByDate(startDate, endDate));
    }
}
