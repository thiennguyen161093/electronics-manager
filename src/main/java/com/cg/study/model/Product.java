package com.cg.study.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private String productDescription;
    @Column(nullable = false)
    private String serviceTag;
    @Column(nullable = false, unique = true)
    private String serialNumber;
    @Column(nullable = false)
    private String purchaseDay;
    private boolean status;

//    @OneToMany(targetEntity = Customer.class, fetch = FetchType.EAGER)
//    private Set<Customer> customers;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
