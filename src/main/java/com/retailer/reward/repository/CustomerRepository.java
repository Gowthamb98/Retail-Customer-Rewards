package com.retailer.reward.repository;

import com.retailer.reward.entity.Customers;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customers,Long> {
        }