package com.retailer.reward.controller;

import com.retailer.reward.entity.PurchaseDetails;
import com.retailer.reward.exception.InvalidIDException;
import com.retailer.reward.service.PurchaseDetailsService;
//import com.retailer.reward.util.DateUtils;
import com.retailer.reward.util.APIResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/purchaseDetails")
public class PurchaseDetailsController {

    @Autowired
    PurchaseDetailsService purchaseDetailsService;

    @PostMapping(value = "/savePurchaseDetails")
    public ResponseEntity<APIResponse> savePurchaseDetails(@Valid @RequestBody PurchaseDetails purchaseDetails) throws Exception {
        return new ResponseEntity<>(purchaseDetailsService.savePurchaseDetails(purchaseDetails), HttpStatus.OK);
    }

    @GetMapping(value = "/retrieveCustomerPurchases")
    public ResponseEntity<APIResponse> retrieveCustomerPurchases(@RequestHeader(required = true) String customerId,
                                                          @RequestHeader(required = false) boolean pastThreeMonths) throws Exception {
        try {
            
            return new ResponseEntity<>(purchaseDetailsService.retrieveCustomerPurchases(customerId, pastThreeMonths), HttpStatus.OK);
        } catch (DateTimeParseException e) {
            throw new InvalidIDException("Date should be in yyyy-MM-dd format");
        }
    }
}
