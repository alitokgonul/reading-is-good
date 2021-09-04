package com.getir.readingisgood.security;

import java.util.Collections;

import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerSecurityService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Customer customer = customerRepository.findByUsername(username);

        if (customer == null) {
            throw new UsernameNotFoundException("Customer with username: '" + username + "' not found");
        }

        return org.springframework.security.core.userdetails.User.withUsername(username)
                                                                 .password(customer.getPassword())
                                                                 // user roles could be added later
                                                                 .authorities(Collections.emptyList())
                                                                 .accountExpired(false)
                                                                 .accountLocked(false)
                                                                 .credentialsExpired(false)
                                                                 .disabled(false)
                                                                 .build();
    }
}
