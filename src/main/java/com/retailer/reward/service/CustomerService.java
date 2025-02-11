package com.retailer.reward.service;

import com.retailer.reward.entity.Customers;
import com.retailer.reward.exception.UnsuccessfulOperationException;
import com.retailer.reward.repository.CustomerRepository;
import com.retailer.reward.util.ProgramConstants;

import com.retailer.reward.util.APIResponse;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//@Slf4j
public class CustomerService {

	private static final Logger log = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	CustomerRepository customerRepository;

	/**
	 * To Save Customers
	 *
	 * @param customer
	 * @return
	 * @throws Exception
	 */
	public APIResponse saveCustomer(Customers customer) throws Exception {
		try {
			customerRepository.save(customer);
			return new APIResponse(ProgramConstants.SUCCESS, "Customer Saved Successfully!", customer);
		} catch (Exception ex) {
			log.error("Failed to Save Customer ", ex);
			throw new UnsuccessfulOperationException(ex.getLocalizedMessage());
		}
	}

	 /**
     * To Update an Existing Customer
     *
     * @param id
     * @param customer
     * @return
     * @throws Exception
     */
    public APIResponse updateCustomer(Long id, Customers customer) throws Exception {
        try {
            
            Optional<Customers> existingCustomerOpt = customerRepository.findById(id);
            if (existingCustomerOpt.isPresent()) {
                Customers existingCustomer = existingCustomerOpt.get();
                
                existingCustomer.setName(customer.getName());
                existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                customerRepository.save(existingCustomer); 
                return new APIResponse(ProgramConstants.SUCCESS, "Customer Updated Successfully!", existingCustomer);
            } else {
                return new APIResponse(ProgramConstants.FAILURE, "Customer Not Found!", null);
            }
        } catch (Exception ex) {
            log.error("Failed to Update Customer ", ex);
            throw new UnsuccessfulOperationException(ex.getLocalizedMessage());
        }
    }
    
    /**
     * To Partially Update an Existing Customer
     *
     * @param id
     * @param customer
     * @return
     * @throws Exception
     */
    public APIResponse patchCustomer(Long id, Customers customer) throws Exception {
        try {
          
            Optional<Customers> existingCustomerOpt = customerRepository.findById(id);
            if (existingCustomerOpt.isPresent()) {
                Customers existingCustomer = existingCustomerOpt.get();

                
                if (customer.getName() != null) {
                    existingCustomer.setName(customer.getName());
                }
                if (customer.getPhoneNumber() != null) {
                    existingCustomer.setPhoneNumber(customer.getPhoneNumber());
                }

                customerRepository.save(existingCustomer); 
                return new APIResponse(ProgramConstants.SUCCESS, "Customer Partially Updated Successfully!", existingCustomer);
            } else {
                return new APIResponse(ProgramConstants.FAILURE, "Customer Not Found!", null);
            }
        } catch (Exception ex) {
            log.error("Failed to Partially Update Customer ", ex);
            throw new UnsuccessfulOperationException(ex.getLocalizedMessage());
        }
    }
}
