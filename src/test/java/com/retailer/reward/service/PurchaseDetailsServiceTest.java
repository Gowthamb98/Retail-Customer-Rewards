package com.retailer.reward.service;

import com.retailer.reward.entity.Customers;
import com.retailer.reward.entity.PurchaseDetails;
import com.retailer.reward.repository.CustomerRepository;
import com.retailer.reward.repository.PurchaseDetailsRepository;
import com.retailer.reward.util.ProgramConstants;
import com.retailer.reward.util.APIResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseDetailsServiceTest {

	@InjectMocks
	PurchaseDetailsService purchaseDetailsService;

	@Mock
	PurchaseDetailsRepository purchaseDetailsRepository;

	@Mock
	CustomerRepository customerRepository;

	@Test
	void testSavePurchaseDetailsCase1() throws Exception {
		LocalDate purchaseDate = LocalDate.parse("2024-11-01"); 

		PurchaseDetails purchase = new PurchaseDetails("1234", purchaseDate, 120.0);
		Customers customer = new Customers(1234L, "John Doe", "9876543210");

		when(customerRepository.findById(1234L)).thenReturn(Optional.of(customer));
		when(purchaseDetailsRepository.save(any(PurchaseDetails.class))).thenReturn(purchase);

		APIResponse response = purchaseDetailsService.savePurchaseDetails(purchase);
		PurchaseDetails savedPurchase = (PurchaseDetails) response.getDetails();

		assertAll(
				() -> assertNotNull(response),
				() -> assertEquals(ProgramConstants.SUCCESS, response.getStatus()),
				() -> assertEquals(90.0, savedPurchase.getOverallRewards()) 
		);
	}

	@Test
	void testSavePurchaseDetailsCase2() throws Exception {
		LocalDate purchaseDate = LocalDate.parse("2024-11-01");

		PurchaseDetails purchase = new PurchaseDetails("1234", purchaseDate, 100.0);
		Customers customer = new Customers(1234L, "John Doe", "9876543210");

		when(customerRepository.findById(1234L)).thenReturn(Optional.of(customer));
		when(purchaseDetailsRepository.save(any(PurchaseDetails.class))).thenReturn(purchase);

		APIResponse response = purchaseDetailsService.savePurchaseDetails(purchase);
		PurchaseDetails savedPurchase = (PurchaseDetails) response.getDetails();

		assertAll(
				() -> assertNotNull(response),
				() -> assertEquals(ProgramConstants.SUCCESS, response.getStatus()),
				() -> assertEquals(50.0, savedPurchase.getOverallRewards())
		);
	}

	@Test
	void testSavePurchaseDetailsCase3() throws Exception {
		LocalDate purchaseDate = LocalDate.parse("2024-11-01");

		PurchaseDetails purchase = new PurchaseDetails("1234", purchaseDate, 75.0);
		Customers customer = new Customers(1234L, "John Doe", "9876543210");

		when(customerRepository.findById(1234L)).thenReturn(Optional.of(customer));
		when(purchaseDetailsRepository.save(any(PurchaseDetails.class))).thenReturn(purchase);

		APIResponse response = purchaseDetailsService.savePurchaseDetails(purchase);
		PurchaseDetails savedPurchase = (PurchaseDetails) response.getDetails();

		assertAll(
				() -> assertNotNull(response),
				() -> assertEquals(ProgramConstants.SUCCESS, response.getStatus()),
				() -> assertEquals(25.0, savedPurchase.getOverallRewards())
		);
	}

	@Test
	void testSavePurchaseDetailsCase4() throws Exception {
		LocalDate purchaseDate = LocalDate.parse("2024-11-01");

		PurchaseDetails purchase = new PurchaseDetails("1234", purchaseDate, 50.0);
		Customers customer = new Customers(1234L, "John Doe", "9876543210");

		when(customerRepository.findById(1234L)).thenReturn(Optional.of(customer));
		when(purchaseDetailsRepository.save(any(PurchaseDetails.class))).thenReturn(purchase);

		APIResponse response = purchaseDetailsService.savePurchaseDetails(purchase);
		PurchaseDetails savedPurchase = (PurchaseDetails) response.getDetails();

		assertAll(
				() -> assertNotNull(response),
				() -> assertEquals(ProgramConstants.SUCCESS, response.getStatus()),
				() -> assertEquals(0.0, savedPurchase.getOverallRewards())
		);
	}


	@Test
	void retrieveCustomerPurchases() throws Exception {
		
		LocalDate pastDate = LocalDate.parse("2024-11-01");  
		LocalDate withinThreeMonthsDate1 = LocalDate.parse("2025-01-01"); 
		LocalDate withinThreeMonthsDate2 = LocalDate.parse("2025-01-05"); 

		
		when(customerRepository.findById(any())).thenReturn(Optional.of(new Customers(
				1234L, "John Doe", "9876543210"
		)));

		
		when(purchaseDetailsRepository.findByCustomerId(any()))
				.thenReturn(Arrays.asList(
						new PurchaseDetails("1234", withinThreeMonthsDate1, 120.0),
						new PurchaseDetails("1234", withinThreeMonthsDate2, 150.0),
						new PurchaseDetails("1234", pastDate, 200.0)  // Older purchase
				));

		
		when(purchaseDetailsRepository.findByCustomerIdAndPurchaseDateGreaterThanEqual(any(), any()))
				.thenReturn(Arrays.asList(
						new PurchaseDetails("1234", withinThreeMonthsDate1, 120.0),
						new PurchaseDetails("1234", withinThreeMonthsDate2, 150.0)
				));

		// Scenario 1: Fetch all purchases (lastThreeMonths = false)
		APIResponse response1 = purchaseDetailsService.retrieveCustomerPurchases("1234", false);
		Map<String, Object> responseMap1 = (Map<String, Object>) response1.getDetails();

		assertAll(
				() -> assertNotNull(response1),
				() -> assertEquals(3, responseMap1.get(ProgramConstants.TOTAL_PURCHASES)), 
				() -> assertEquals(470.0, responseMap1.get(ProgramConstants.TOTAL_TRANSACTION_AMOUNT))
		);

		// Scenario 2: Fetch only last 3 months purchases (lastThreeMonths = true)
		APIResponse response2 = purchaseDetailsService.retrieveCustomerPurchases("1234", true);
		Map<String, Object> responseMap2 = (Map<String, Object>) response2.getDetails();

		assertAll(
				() -> assertNotNull(response2),
				() -> assertEquals(2, responseMap2.get(ProgramConstants.TOTAL_PURCHASES)), 
				() -> assertEquals(270.0, responseMap2.get(ProgramConstants.TOTAL_TRANSACTION_AMOUNT)) 
		);
	}



}
