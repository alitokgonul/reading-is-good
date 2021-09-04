package com.getir.readingisgood.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class CustomerSecurityServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerSecurityService customerSecurityService;

    @Test
    void loadUserByUsername() {
        // given
        given(customerRepository.findByUsername(anyString())).willReturn(createCustomer());

        // when
        UserDetails userDetails = customerSecurityService.loadUserByUsername("user");

        // then
        assertEquals(userDetails.getUsername(), "user");
        assertEquals(userDetails.getPassword(), "1234");
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_user_not_found() {
        // given
        given(customerRepository.findByUsername(anyString())).willReturn(null);

        // when
        assertThrows(UsernameNotFoundException.class, () -> customerSecurityService.loadUserByUsername("user"));
        verify(customerRepository).findByUsername(anyString());
    }

    private Customer createCustomer() {
        Customer customer = new Customer();
        customer.setUsername("user");
        customer.setId(1L);
        customer.setEmail("user@mail.com");
        customer.setPhone("5555555555");
        customer.setLastName("lastName");
        customer.setName("name");
        customer.setPassword("1234");
        return customer;
    }
}