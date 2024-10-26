package com.yassir.bank.balance.rest;

import com.yassir.bank.balance.dto.CustomerRequest;
import com.yassir.bank.balance.dto.CustomerResponse;
import com.yassir.bank.balance.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    /**
     *
     * @param customer
     * @return a 201 createdStatus with CustomerResponse The saved Customer
     */
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerRequest customer) {
        log.debug("REST request to save a new Customer: {}", customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                customerService.save(customer)
        );
    }

    /**
     *
     * @param pageable the start page and the page size
     * @param search not required
     * @return status 200 with a page of Customers
     */
    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll(Pageable pageable, @RequestParam(required = false) String search) {
        log.debug("REST request to get a page of CustomerResponse , start from : {}, page size is: {}, search by: {}", pageable.getPageNumber(), pageable.getPageSize(), search);
        Page<CustomerResponse> page = customerService.findAll(pageable, search);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Total-Elements", String.valueOf(page.getTotalElements()));
        headers.add("Total-Pages", String.valueOf(page.getTotalPages()));

        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
