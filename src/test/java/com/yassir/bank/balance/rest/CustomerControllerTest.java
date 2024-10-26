package com.yassir.bank.balance.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yassir.bank.balance.dto.CustomerRequest;
import com.yassir.bank.balance.dto.CustomerResponse;
import com.yassir.bank.balance.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
    }

    @Test
    void createCustomer_ShouldReturnCreatedCustomer() throws Exception {
        // Arrange
        CustomerRequest customerRequest = new CustomerRequest("John Doe"); // Sample name
        CustomerResponse customerResponse = new CustomerResponse(1L, "John Doe"); // Example initialization

        when(customerService.save(any(CustomerRequest.class))).thenReturn(customerResponse);

        // Act & Assert
        mockMvc.perform(post("/v1/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(customerResponse.id()));
    }

    @Test
    void findAll_ShouldReturnCustomerList() throws Exception {
        // Arrange
        CustomerResponse customerResponse = new CustomerResponse(1L, "John Doe"); // Example initialization
        List<CustomerResponse> customerResponses = List.of(customerResponse);
        // Mocking the pagination behavior
        when(customerService.findAll(any(), any())).thenReturn(new PageImpl<>(customerResponses));

        // Act & Assert
        mockMvc.perform(get("/v1/api/customers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(customerResponse.id()))
                .andExpect(header().string("Total-Elements", "1"))
                .andExpect(header().string("Total-Pages", "1"));
    }
}