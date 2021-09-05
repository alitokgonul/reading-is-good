package com.getir.readingisgood;

import com.getir.readingisgood.entity.Customer;
import com.getir.readingisgood.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class ReadingIsGoodApplication implements CommandLineRunner {

    private final CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(ReadingIsGoodApplication.class, args);
    }

    @Override
    public void run(String... params) {
        Customer customer = new Customer();
        customer.setUsername("test");
        customer.setPassword("test");
        customer.setEmail("test@mail.com");
        customer.setId(1L);
        customer.setName("Ali");
        customer.setLastName("Tokgönül");
        customer.setPhone("5555555555");
        customerService.register(customer);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
