package com.retailer.reward.controller;

import com.retailer.reward.entity.PurchaseDetails;
import com.retailer.reward.service.PurchaseDetailsService;
import com.retailer.reward.util.APIResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchaseDetails")
public class PurchaseDetailsController {

    @Autowired
    PurchaseDetailsService purchaseDetailsService;

    @PostMapping(value = "/savePurchaseDetails")
    public ResponseEntity<APIResponse> savePurchaseDetails(@Valid @RequestBody PurchaseDetails purchaseDetails) throws Exception {
        return new ResponseEntity<>(purchaseDetailsService.savePurchaseDetails(purchaseDetails), HttpStatus.OK);
    }

    @GetMapping("/retrieveCustomerPurchases/{customerId}")
    public ResponseEntity<APIResponse> retrieveCustomerPurchases(
            @PathVariable String customerId,
            @RequestParam(required = false, defaultValue = "false") boolean pastThreeMonths) throws Exception {

        APIResponse response = purchaseDetailsService.retrieveCustomerPurchases(customerId, pastThreeMonths);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
