package com.getir.readingisgood.service;

import javax.servlet.http.HttpServletRequest;

import com.getir.readingisgood.controller.model.UserDTO;
import com.getir.readingisgood.entity.Customer;

public interface CustomerService {

    UserDTO register(Customer customer);

    UserDTO login(String username, String password);

    Customer getCustomerInfo(HttpServletRequest req);
}
