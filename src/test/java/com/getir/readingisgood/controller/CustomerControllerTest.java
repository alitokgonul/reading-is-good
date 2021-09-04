package com.getir.readingisgood.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.getir.readingisgood.controller.model.CustomerDTO;
import com.getir.readingisgood.controller.model.UserDTO;
import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
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
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(new CustomerController(customerService, modelMapper)).build();
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
        mockMvc.perform(post(CUSTOMER_ENDPOINT
                             + "/login?password=user").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
               .andReturn();
    }

    @Test
    void login_missing_password() throws Exception {
        // when / then
        mockMvc.perform(post(CUSTOMER_ENDPOINT
                             + "/login?username=user").contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isBadRequest())
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