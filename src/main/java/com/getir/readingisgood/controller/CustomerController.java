package com.getir.readingisgood.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.getir.readingisgood.controller.model.CustomerDTO;
import com.getir.readingisgood.controller.model.OrderDTO;
import com.getir.readingisgood.controller.model.UserDTO;
import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.service.CustomerService;
import com.getir.readingisgood.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customers")
@Api(tags = { "Customer" })
@SwaggerDefinition(tags = { @Tag(name = "Customer", description = "Customer management") })
public class CustomerController {

    private final CustomerService customerService;
    private final OrderService orderService;
    private final ModelMapper modelMapper;

    @ApiOperation(value = "Register new customer")
    @ApiResponses(value = { @ApiResponse(code = 201, message = "The new customer is successfully added."),
                            @ApiResponse(code = 400, message = "Please check your request"),
                            @ApiResponse(code = 401, message = "Authorization error"),
                            @ApiResponse(code = 500, message = "Unexpected server error") })
    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody @Valid CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.register(modelMapper.map(customerDTO, Customer.class)));
    }

    @PostMapping("/login")
    @ApiOperation(value = "Authenticates customer and returns its JWT token and username.")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "The customer successfully logged in."),
                            @ApiResponse(code = 400, message = "Something went wrong"),
                            @ApiResponse(code = 422, message = "Invalid username/password supplied") })
    public ResponseEntity<UserDTO> login(@ApiParam("username") @RequestParam String username,
                                         @ApiParam("password") @RequestParam String password) {
        return ResponseEntity.ok(customerService.login(username, password));
    }

    @ApiOperation(value = "List all orders of the customer")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Orders of the customer are successfully retrieved."),
                            @ApiResponse(code = 404,
                                message = "Accessing the resource you were trying to reach is not found"),
                            @ApiResponse(code = 500, message = "The conversions could not be retrieved.") })
    @GetMapping(value = "list-orders", produces = "application/json")
    public ResponseEntity<Page<OrderDTO>> listOrders(HttpServletRequest req,
                                                     @RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size) {

        return ResponseEntity.ok(orderService.listOrders(req, page, size));
    }
}
