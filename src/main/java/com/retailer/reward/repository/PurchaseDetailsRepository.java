package com.retailer.reward.repository;

import com.retailer.reward.entity.PurchaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PurchaseDetailsRepository extends JpaRepository<PurchaseDetails, Long> {

    List<PurchaseDetails> findByCustomerIdAndPurchaseDateBetween(String customerId, LocalDate fromDate, LocalDate toDate);


    List<PurchaseDetails> findByCustomerIdAndPurchaseDateGreaterThanEqual(String customerId, LocalDate fromDate);

    List<PurchaseDetails> findByCustomerId(String customerId);
}