package com.getir.readingisgood.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Setter
@Getter
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private String username;
    private String password;
    private String email;
    private String phone;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<OrderDetail> orderList;

    @CreationTimestamp
    private LocalDateTime createdDateTime;
}
