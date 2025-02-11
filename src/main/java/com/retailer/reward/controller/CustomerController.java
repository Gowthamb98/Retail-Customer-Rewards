package com.retailer.reward.controller;

import com.retailer.reward.entity.Customers;
import com.retailer.reward.service.CustomerService;
import com.retailer.reward.util.APIResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	@Autowired
	CustomerService customerService;

	/**
	 * To Save Customers
	 *
	 * @param customers
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/saveCustomer")
	public ResponseEntity<APIResponse> saveCustomer(@Valid @RequestBody Customers customers) throws Exception {
		return new ResponseEntity<>(customerService.saveCustomer(customers), HttpStatus.OK);
}
	/**
     * To Update an Existing Customer
     *
     * @param id
     * @param customers
     * @return
     * @throws Exception
     */
    @PutMapping(value = "/updateCustomer/{id}")
    public ResponseEntity<APIResponse> updateCustomer(@PathVariable Long id, @Valid @RequestBody Customers customers) throws Exception {
        return new ResponseEntity<>(customerService.updateCustomer(id, customers), HttpStatus.OK);
    }
    
    /**
     * To Partially Update an Existing Customer
     *
     * @param id
     * @param customers
     * @return
     * @throws Exception
     */
    @PatchMapping(value = "/patchCustomer/{id}")
    public ResponseEntity<APIResponse> patchCustomer(@PathVariable Long id, @RequestBody Customers customers) throws Exception {
        return new ResponseEntity<>(customerService.patchCustomer(id, customers), HttpStatus.OK);
    }

}