package com.retailer.reward.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.retailer.reward.entity.Customers;
import com.retailer.reward.service.CustomerService;
import com.retailer.reward.util.ProgramConstants;
import com.retailer.reward.util.APIResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @InjectMocks
    private CustomerController controller;

    @Mock
    private CustomerService customerService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


    @Test
    public void testSaveCustomer() throws Exception {
        Customers customers = new Customers(1234L, "name", "9876543210");
        when(customerService.saveCustomer(any(Customers.class))).thenReturn(new APIResponse(ProgramConstants.SUCCESS, "Customer Saved Successfully!", customers));
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/customer/saveCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customers))
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(request).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ProgramConstants.SUCCESS));

    }

    ObjectMapper mapper;


    @Test
    public void testSaveCustomer_ValidInputs() throws Exception {
        // Scenario 1 - null Name values
        Customers customers = new Customers(1234L, null, "1234567890");
        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/customer/saveCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customers))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) result.getResolvedException();

        assertNotNull(methodArgumentNotValidException);
        assertNotNull(methodArgumentNotValidException.getFieldErrors());
        assertEquals("name", methodArgumentNotValidException.getFieldErrors().get(0).getField());
        assertEquals("must not be null", methodArgumentNotValidException.getFieldErrors().get(0).getDefaultMessage());


        // Scenario 2 - Invalid Mobile number
        customers = new Customers(1234L, "test", "123456789");
        request = MockMvcRequestBuilders
                .post("/api/customer/saveCustomer")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(customers))
                .contentType(MediaType.APPLICATION_JSON);
        result = mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
        methodArgumentNotValidException = (MethodArgumentNotValidException) result.getResolvedException();
        assertNotNull(methodArgumentNotValidException);
        assertNotNull(methodArgumentNotValidException.getFieldErrors());
        assertEquals("phoneNumber", methodArgumentNotValidException.getFieldErrors().get(0).getField());
        assertEquals("Phone number should have 10 digits", methodArgumentNotValidException.getFieldErrors().get(0).getDefaultMessage());
    }


    @Test
    public void testUpdateCustomer_Success() throws Exception {
        Long customerId = 1234L;
        Customers existingCustomer = new Customers(customerId, "oldName", "9876543210");
        Customers updatedCustomer = new Customers(customerId, "newName", "9876543210");

        when(customerService.updateCustomer(any(Long.class), any(Customers.class)))
                .thenReturn(new APIResponse(ProgramConstants.SUCCESS, "Customer Updated Successfully!", updatedCustomer));

        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/customer/updateCustomer/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedCustomer))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ProgramConstants.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer Updated Successfully!"));
    }


    @Test
    public void testUpdateCustomer_CustomerNotFound() throws Exception {
        Long customerId = 1234L;
        Customers updatedCustomer = new Customers(customerId, "newName", "9876543210");

        when(customerService.updateCustomer(any(Long.class), any(Customers.class)))
                .thenReturn(new APIResponse(ProgramConstants.FAILURE, "Customer Not Found!", null));

        RequestBuilder request = MockMvcRequestBuilders
                .put("/api/customer/updateCustomer/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(updatedCustomer))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ProgramConstants.FAILURE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer Not Found!"));
    }


    @Test
    public void testPatchCustomer_Success() throws Exception {
        Long customerId = 1234L;
        Customers existingCustomer = new Customers(customerId, "oldName", "9876543210");
        Customers patchedCustomer = new Customers(customerId, "newName", null);

        when(customerService.patchCustomer(any(Long.class), any(Customers.class)))
                .thenReturn(new APIResponse(ProgramConstants.SUCCESS, "Customer Partially Updated Successfully!", patchedCustomer));

        RequestBuilder request = MockMvcRequestBuilders
                .patch("/api/customer/patchCustomer/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patchedCustomer))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ProgramConstants.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer Partially Updated Successfully!"));
    }


    @Test
    public void testPatchCustomer_CustomerNotFound() throws Exception {
        Long customerId = 1234L;
        Customers patchedCustomer = new Customers(customerId, "newName", null);

        when(customerService.patchCustomer(any(Long.class), any(Customers.class)))
                .thenReturn(new APIResponse(ProgramConstants.FAILURE, "Customer Not Found!", null));

        RequestBuilder request = MockMvcRequestBuilders
                .patch("/api/customer/patchCustomer/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(patchedCustomer))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ProgramConstants.FAILURE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Customer Not Found!"));
    }
}