package com.getir.readingisgood.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.getir.readingisgood.controller.model.CustomerDTO;
import com.getir.readingisgood.controller.model.UserDTO;
import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.service.CustomerService;
import com.getir.readingisgood.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    private static final String CUSTOMER_ENDPOINT = "/api/v1/customers";
    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9";
    private static final String USER_NAME = "test";

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CustomerService customerService;

    @Mock
    private OrderService orderService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc =
            MockMvcBuilders.standaloneSetup(new CustomerController(customerService, orderService, modelMapper)).build();
    }

    @Test
    void register_success() throws Exception {
        // given
        given(customerService.register(any(Customer.class))).willReturn(createUserDTO());
        given(modelMapper.map(any(), any())).willReturn(createCustomer());
        CustomerDTO request = createCustomerDTO("user@mail.com", "5555555555", "user");
        String createRequestStr = objectMapper.writeValueAsString(request);

        // when
        MvcResult mvcResult =
            mockMvc.perform(post(CUSTOMER_ENDPOINT + "/register").contentType(MediaType.APPLICATION_JSON)
                                                                 .content(createRequestStr))
                   .andExpect(status().isOk())
                   .andReturn();

        // then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        UserDTO dto = objectMapper.readValue(contentAsString, UserDTO.class);

        assertEquals(dto.getUsername(), USER_NAME);
        assertEquals(dto.getToken(), TOKEN);
    }

    @Test
    void register_invalid_mail() throws Exception {
        // given
        CustomerDTO request = createCustomerDTO("usermail.com", "5555555555", "user");
        String createRequestStr = objectMapper.writeValueAsString(request);

        // when / then
        mockMvc.perform(post(CUSTOMER_ENDPOINT + "/register").contentType(MediaType.APPLICATION_JSON)
                                                             .content(createRequestStr))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void register_invalid_phone() throws Exception {
        // given
        CustomerDTO request = createCustomerDTO("user@mail.com", "a5555555555", "user");
        String createRequestStr = objectMapper.writeValueAsString(request);

        // when / then
        mockMvc.perform(post(CUSTOMER_ENDPOINT + "/register").contentType(MediaType.APPLICATION_JSON)
                                                             .content(createRequestStr))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void register_null_username() throws Exception {
        // given
        CustomerDTO request = createCustomerDTO("user@mail.com", "a5555555555", null);
        String createRequestStr = objectMapper.writeValueAsString(request);

        // when / then
        mockMvc.perform(post(CUSTOMER_ENDPOINT + "/register").contentType(MediaType.APPLICATION_JSON)
                                                             .content(createRequestStr))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void login_success() throws Exception {
        // given
        given(customerService.login(anyString(), anyString())).willReturn(createUserDTO());

        // when
        MvcResult mvcResult = mockMvc.perform(post(CUSTOMER_ENDPOINT
                                                   + "/login?password=user&username=test").contentType(MediaType.APPLICATION_JSON))
                                     .andExpect(status().isOk())
                                     .andReturn();

        // then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        UserDTO dto = objectMapper.readValue(contentAsString, UserDTO.class);

        assertEquals(dto.getUsername(), USER_NAME);
        assertEquals(dto.getToken(), TOKEN);
    }

    @Test
    void login_missing_username() throws Exception {
        // when / then
        mockMvc.perform(post(CUSTOMER_ENDPOINT + "/login?password=user").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void login_missing_password() throws Exception {
        // when / then
        mockMvc.perform(post(CUSTOMER_ENDPOINT + "/login?username=user").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void listOrders() throws Exception {
        // given
        given(orderService.listOrders(any(), anyInt(), anyInt())).willReturn(new PageImpl<>(Collections.emptyList()));

        // when / then
        mockMvc.perform(get(CUSTOMER_ENDPOINT + "/list-orders").accept(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andReturn();
    }

    private UserDTO createUserDTO() {
        return UserDTO.builder().username(USER_NAME).token(TOKEN).build();
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setEmail("user@mail.com");
        customer.setId(1L);
        customer.setUsername("user");
        return customer;
    }

    private CustomerDTO createCustomerDTO(String email, String phone, String username) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail(email);
        customerDTO.setPhone(phone);
        customerDTO.setName("userName");
        customerDTO.setLastName("userLastName");
        customerDTO.setPassword("1111");
        customerDTO.setUsername(username);
        return customerDTO;
    }
}