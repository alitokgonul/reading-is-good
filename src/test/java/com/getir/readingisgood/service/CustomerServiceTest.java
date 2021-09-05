package com.getir.readingisgood.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.servlet.http.HttpServletRequest;

import com.getir.readingisgood.controller.model.UserDTO;
import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.exception.CustomException;
import com.getir.readingisgood.repository.CustomerRepository;
import com.getir.readingisgood.security.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9";
    private static final String USER_NAME = "user";

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void register_success() {
        // given
        given(customerRepository.existsByUsername(anyString())).willReturn(false);
        given(jwtTokenProvider.createToken(anyString())).willReturn(TOKEN);

        // when
        UserDTO userDTO = customerService.register(createCustomer());

        // then
        assertEquals(userDTO.getUsername(), USER_NAME);
        assertEquals(userDTO.getToken(), TOKEN);
    }

    @Test
    void register_fail() {
        // given
        given(customerRepository.existsByUsername(anyString())).willReturn(true);

        // when / then
        assertThrows(CustomException.class, () -> customerService.register(createCustomer()));
        verify(customerRepository).existsByUsername(anyString());
    }

    @Test
    void login() {
        // given
        given(jwtTokenProvider.createToken(anyString())).willReturn(TOKEN);

        // when
        UserDTO userDTO = customerService.login(USER_NAME, anyString());

        // then
        assertEquals(userDTO.getUsername(), USER_NAME);
        assertEquals(userDTO.getToken(), TOKEN);
    }

    @Test
    void login_fail() {
        // given
        given(authenticationManager.authenticate(any())).willThrow(new AuthenticationException("error") {
        });

        // when / then
        assertThrows(CustomException.class, () -> customerService.login(USER_NAME, anyString()));
        verify(authenticationManager).authenticate(any());
    }

    @Test
    void getCustomerInfo() {
        // given
        final Customer customer = createCustomer();
        HttpServletRequest req = mock(HttpServletRequest.class);
        given(customerRepository.findByUsername(anyString())).willReturn(customer);
        given(jwtTokenProvider.resolveToken(req)).willReturn(customer.getUsername());
        given(jwtTokenProvider.getUsername(anyString())).willReturn(customer.getUsername());

        // when
        Customer returnedCustomer = customerService.getCustomerInfo(req);

        // then
        assertEquals(returnedCustomer.getUsername(), customer.getUsername());
        assertEquals(returnedCustomer.getEmail(), customer.getEmail());
        assertEquals(returnedCustomer.getPassword(), customer.getPassword());
        assertEquals(returnedCustomer.getLastName(), customer.getLastName());
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setUsername(USER_NAME);
        customer.setId(1L);
        customer.setEmail("user@mail.com");
        customer.setPhone("5555555555");
        customer.setLastName("lastName");
        customer.setName("name");
        return customer;
    }
}