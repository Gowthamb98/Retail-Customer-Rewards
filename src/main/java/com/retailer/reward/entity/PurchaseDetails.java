package com.retailer.reward.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;


@Entity
public class PurchaseDetails {
	public PurchaseDetails(String customerId, LocalDate purchaseDate, Double transactionAmount) {
		this.customerId = customerId;
		this.purchaseDate = purchaseDate;
		this.transactionAmount = transactionAmount;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	String customerId;
	
	@NotNull
	LocalDate purchaseDate;
	@NotNull
	Double transactionAmount = 0.0;
	Double overallRewards = 0.0;

	


	public PurchaseDetails() {
	}

	public PurchaseDetails(Long id, String customerId, LocalDate purchaseDate, Double transactionAmount, Double overallRewards) {
		this.id = id;
		this.customerId = customerId;
		this.purchaseDate = purchaseDate;
		this.transactionAmount = transactionAmount;
		this.overallRewards = overallRewards;
		
		}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	
	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double totalOrder) {
		this.transactionAmount = totalOrder;
	}

	public Double getOverallRewards() {
		return overallRewards;
	}

	public void setOverallRewards(Double overallRewards) {
		this.overallRewards = overallRewards;
	}


}