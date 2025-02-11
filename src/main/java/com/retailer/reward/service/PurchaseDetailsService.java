package com.retailer.reward.service;

import com.retailer.reward.entity.Customers;
import com.retailer.reward.entity.PurchaseDetails;
import com.retailer.reward.exception.InvalidIDException;
import com.retailer.reward.exception.UnsuccessfulOperationException;
import com.retailer.reward.repository.CustomerRepository;
import com.retailer.reward.repository.PurchaseDetailsRepository;
import com.retailer.reward.util.ProgramConstants;
import com.retailer.reward.util.APIResponse;
import com.retailer.reward.util.RewardPointsCalc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseDetailsService {
    private static final Logger log = LoggerFactory.getLogger(PurchaseDetailsService.class);

    @Autowired
    PurchaseDetailsRepository purchaseDetailsRepository;

    @Autowired
    CustomerRepository customerRepository;

    public APIResponse savePurchaseDetails(PurchaseDetails purchaseDetails) throws Exception {
        Long customerId = parseCustomerId(purchaseDetails.getCustomerId());

        if (customerRepository.findById(customerId).isEmpty()) {
            throw new InvalidIDException("Invalid Customer ID -" + purchaseDetails.getCustomerId());
        }
        try {
            RewardPointsCalc.computeRewardPoints(purchaseDetails);
            purchaseDetailsRepository.save(purchaseDetails);
            return new APIResponse(ProgramConstants.SUCCESS, "Purchase Details Saved!", purchaseDetails);
        } catch (Exception e) {
            log.error("Failed to Save Purchase Details ", e);
            throw new UnsuccessfulOperationException(e.getLocalizedMessage());
        }
    }

    public APIResponse retrieveCustomerPurchases(String customerId, boolean lastThreeMonths) throws Exception {
        Long id = parseCustomerId(customerId);
        Optional<Customers> customer = customerRepository.findById(id);
        if (customer.isEmpty()) {
            throw new InvalidIDException("Invalid Customer ID -" + customerId);
        }

        List<PurchaseDetails> purchaseDetailsList;
          if (lastThreeMonths ) {
        	LocalDate pastThreeMonths = LocalDate.now().minusMonths(3);
            purchaseDetailsList = purchaseDetailsRepository.findByCustomerIdAndPurchaseDateGreaterThanEqual(customerId,pastThreeMonths);
        } else {
            purchaseDetailsList = purchaseDetailsRepository.findByCustomerId(customerId);
        }

        try {
            if (purchaseDetailsList.isEmpty()) {
                return new APIResponse(ProgramConstants.SUCCESS, "Purchase Details Not Found!");
            } else {
                double totalTransactionValue = purchaseDetailsList.stream().mapToDouble(PurchaseDetails::getTransactionAmount).sum();
                double overallRewardsValue = purchaseDetailsList.stream().mapToDouble(PurchaseDetails::getOverallRewards).sum();
                Map<String, Double> monthlyRewards = purchaseDetailsList.stream()
                        .collect(Collectors.groupingBy(
                                p -> p.getPurchaseDate().getMonth() + "-" + p.getPurchaseDate().getYear(), 
                                LinkedHashMap::new,
                                Collectors.summingDouble(PurchaseDetails::getOverallRewards)));
                Map<String, Object> purchaseHistory = new LinkedHashMap<>();
                purchaseHistory.put(ProgramConstants.CUSTOMER_NAME, customer.get().getName());
                purchaseHistory.put(ProgramConstants.CUSTOMER_PHONENUMBER, customer.get().getPhoneNumber());
                purchaseHistory.put(ProgramConstants.TOTAL_PURCHASES, purchaseDetailsList.size());
                purchaseHistory.put(ProgramConstants.TOTAL_TRANSACTION_AMOUNT, totalTransactionValue);
                purchaseHistory.put(ProgramConstants.OVERALL_REWARD_POINTS, overallRewardsValue);
                purchaseHistory.put(ProgramConstants.MONTHLY_REWARDS, monthlyRewards);
                purchaseHistory.put(ProgramConstants.PURCHASE_DETAILS, purchaseDetailsList);
                return new APIResponse(purchaseHistory);
            }
        } catch (Exception e) {
            log.error("Failed to Retrieve Purchase Details ", e);
            throw new UnsuccessfulOperationException(e.getLocalizedMessage());
        }
    }

    private Long parseCustomerId(String customerId) throws NumberFormatException {
        return Long.parseLong(customerId);
    }

    public static LocalDate parseDateFromString(String date) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, formatter);
    }
}
