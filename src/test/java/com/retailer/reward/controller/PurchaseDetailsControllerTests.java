package com.retailer.reward.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.retailer.reward.entity.PurchaseDetails;
import com.retailer.reward.service.PurchaseDetailsService;
import com.retailer.reward.util.APIResponse;
import com.retailer.reward.util.ProgramConstants;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PurchaseDetailsControllerTests {

    @InjectMocks
    private PurchaseDetailsController controller;

    @Mock
    private PurchaseDetailsService purchaseDetailsService;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testSavePurchaseDetails() throws Exception {
        PurchaseDetails purchaseDetails = new PurchaseDetails("1", LocalDate.of(2025, 1, 1), 120.0);

        when(purchaseDetailsService.savePurchaseDetails(any(PurchaseDetails.class)))
                .thenReturn(new APIResponse(ProgramConstants.SUCCESS, "Purchase Details Saved!", purchaseDetails));

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/purchaseDetails/savePurchaseDetails")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(purchaseDetails))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ProgramConstants.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Purchase Details Saved!"));
    }

    @Test
    public void testSavePurchaseDetails_ValidInputs() throws Exception {
        PurchaseDetails purchaseDetails = new PurchaseDetails(null, LocalDate.of(2025, 1, 1), 120.0);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/purchaseDetails/savePurchaseDetails")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(purchaseDetails))
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) result.getResolvedException();
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("customerId"));
    }

    @Test
    public void testRetrieveCustomerPurchases() throws Exception {
        List<PurchaseDetails> purchaseDetailsList = Arrays.asList(
                new PurchaseDetails("1", LocalDate.of(2025, 1, 1), 120.0),
                new PurchaseDetails("1", LocalDate.of(2025, 1, 5), 120.0)
        );
        Map<String, Double> monthlyRewards = purchaseDetailsList.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getPurchaseDate().getMonth() + "-" + p.getPurchaseDate().getYear(), 
                        LinkedHashMap::new,
                        Collectors.summingDouble(PurchaseDetails::getOverallRewards) 
                ));
        Map<String, Object> purchaseHistory = new LinkedHashMap<>();
        purchaseHistory.put(ProgramConstants.CUSTOMER_NAME, "Test Customer");
        purchaseHistory.put(ProgramConstants.CUSTOMER_PHONENUMBER, "9876543210");
        purchaseHistory.put(ProgramConstants.TOTAL_PURCHASES, purchaseDetailsList.size());
        purchaseHistory.put(ProgramConstants.TOTAL_TRANSACTION_AMOUNT, 240.0);
        purchaseHistory.put(ProgramConstants.OVERALL_REWARD_POINTS, 180.0);
        purchaseHistory.put(ProgramConstants.MONTHLY_REWARDS, monthlyRewards);
        purchaseHistory.put(ProgramConstants.PURCHASE_DETAILS, purchaseDetailsList);

        when(purchaseDetailsService.retrieveCustomerPurchases("1", false))
                .thenReturn(new APIResponse(ProgramConstants.SUCCESS, "Details Fetched Successfully!", purchaseHistory));

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/purchaseDetails/retrieveCustomerPurchases")
                .header("customerId", "1")
                .header("pastThreeMonths", false);

        mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(ProgramConstants.SUCCESS))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Details Fetched Successfully!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.details.PurchaseDetails.length()").value(2));
    }
}
