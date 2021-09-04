package com.getir.readingisgood.service;

import javax.transaction.Transactional;

import com.getir.readingisgood.controller.model.UserDTO;
import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.exception.CustomException;
import com.getir.readingisgood.repository.CustomerRepository;
import com.getir.readingisgood.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public UserDTO register(final Customer customer) {
        if (!customerRepository.existsByUsername(customer.getUsername())) {
            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customerRepository.save(customer);
            log.info("Customer with userName {0}, successfully saved.", customer.getUsername());
            return UserDTO.builder()
                          .username(customer.getUsername())
                          .token(jwtTokenProvider.createToken(customer.getUsername()))
                          .build();
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Override
    @Transactional
    public UserDTO login(final String username, final String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return UserDTO.builder().username(username).token(jwtTokenProvider.createToken(username)).build();
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
