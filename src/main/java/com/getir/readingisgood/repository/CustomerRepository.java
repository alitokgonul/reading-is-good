package com.getir.readingisgood.repository;

import com.getir.readingisgood.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByUsername(String username);

    Customer findByUsername(String username);

}
