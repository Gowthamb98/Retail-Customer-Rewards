package com.retailer.reward.service;

import com.retailer.reward.entity.Customers;
import com.retailer.reward.exception.UnsuccessfulOperationException;
import com.retailer.reward.repository.CustomerRepository;
import com.retailer.reward.util.ProgramConstants;
import com.retailer.reward.util.APIResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    CustomerService mockCustomerService;

    @Mock
    CustomerRepository customerRepository;


    @Test
    void testSaveCustomer() throws Exception {
        Customers customers = new Customers(1234L, "name", "987654321");
        when(customerRepository.save(Mockito.any(Customers.class))).thenReturn(customers);
        APIResponse response = mockCustomerService.saveCustomer(customers);
        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ProgramConstants.SUCCESS, response.getStatus())
        );
    }

    @Test
    void testUpdateCustomer() throws Exception {
        Long customerId = 1234L;
        Customers existingCustomer = new Customers(customerId, "oldName", "9876543210");
        Customers updatedCustomer = new Customers(customerId, "newName", "9876543210");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(Mockito.any(Customers.class))).thenReturn(updatedCustomer);

        APIResponse response = mockCustomerService.updateCustomer(customerId, updatedCustomer);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ProgramConstants.SUCCESS, response.getStatus()),
                () -> assertEquals("Customer Updated Successfully!", response.getMessage()),
                () -> assertEquals(updatedCustomer.getName(), ((Customers) response.getDetails()).getName())
        );
    }

    @Test
    void testUpdateCustomer_CustomerNotFound() throws Exception {
        Long customerId = 1234L;
        Customers updatedCustomer = new Customers(customerId, "newName", "9876543210");

        
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        APIResponse response = mockCustomerService.updateCustomer(customerId, updatedCustomer);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ProgramConstants.FAILURE, response.getStatus()),
                () -> assertEquals("Customer Not Found!", response.getMessage()),
                () -> assertNull(response.getDetails()) // No customer data returned
        );
    }

    @Test
    void testPatchCustomer() throws Exception {
        Long customerId = 1234L;
        Customers existingCustomer = new Customers(customerId, "oldName", "9876543210");
        Customers patchedCustomer = new Customers(customerId, "newName", null);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));
        when(customerRepository.save(Mockito.any(Customers.class))).thenReturn(patchedCustomer);

        APIResponse response = mockCustomerService.patchCustomer(customerId, patchedCustomer);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ProgramConstants.SUCCESS, response.getStatus()),
                () -> assertEquals("Customer Partially Updated Successfully!", response.getMessage()),
                () -> assertEquals(patchedCustomer.getName(), ((Customers) response.getDetails()).getName()),
                () -> assertEquals(existingCustomer.getPhoneNumber(), ((Customers) response.getDetails()).getPhoneNumber()) 
        );
    }


    @Test
    void testPatchCustomer_CustomerNotFound() throws Exception {
        Long customerId = 1234L;
        Customers patchedCustomer = new Customers(customerId, "newName", null);

        
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        APIResponse response = mockCustomerService.patchCustomer(customerId, patchedCustomer);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(ProgramConstants.FAILURE, response.getStatus()),
                () -> assertEquals("Customer Not Found!", response.getMessage()),
                () -> assertNull(response.getDetails())
        );
    }


    @Test
    void testUpdateCustomer_ExceptionDuringSave() throws Exception {
        Long customerId = 1234L;
        Customers existingCustomer = new Customers(customerId, "oldName", "9876543210");
        Customers updatedCustomer = new Customers(customerId, "newName", "9876543210");

      
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

       
        when(customerRepository.save(Mockito.any(Customers.class))).thenThrow(new RuntimeException("Database error"));

      
        try {
            mockCustomerService.updateCustomer(customerId, updatedCustomer);
            fail("Expected exception to be thrown");
        } catch (Exception ex) {
            assertTrue(ex instanceof UnsuccessfulOperationException);
            assertEquals("Database error", ex.getLocalizedMessage());
        }
    }

    @Test
    void testPatchCustomer_ExceptionDuringSave() throws Exception {
        Long customerId = 1234L;
        Customers existingCustomer = new Customers(customerId, "oldName", "9876543210");
        Customers patchedCustomer = new Customers(customerId, "newName", null);

   
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(existingCustomer));

        
        when(customerRepository.save(Mockito.any(Customers.class))).thenThrow(new RuntimeException("Database error"));

       
        try {
            mockCustomerService.patchCustomer(customerId, patchedCustomer);
            fail("Expected exception to be thrown");
        } catch (Exception ex) {
            assertTrue(ex instanceof UnsuccessfulOperationException);
            assertEquals("Database error", ex.getLocalizedMessage());
        }
    }




}