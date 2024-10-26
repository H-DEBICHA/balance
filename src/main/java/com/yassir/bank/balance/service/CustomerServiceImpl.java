package com.yassir.bank.balance.service;

import com.yassir.bank.balance.dto.CustomerRequest;
import com.yassir.bank.balance.dto.CustomerResponse;
import com.yassir.bank.balance.mapper.CustomerMapper;
import com.yassir.bank.balance.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public CustomerResponse save(CustomerRequest request) {
        log.debug("Request to save a new customer: {}", request);
        return customerMapper.toDto(
                customerRepository.save(
                        customerMapper.toCustomer(request)
                )
        );
    }

    @Override
    public Page<CustomerResponse> findAll(Pageable pageable, String search) {
        log.debug("Request to get a page of Customers, start from : {}, page size is: {}, search by: {}", pageable.getPageNumber(), pageable.getPageSize(), search);
        if (search != null && !search.isBlank())
            return customerRepository.findByNameContainingIgnoreCase(pageable, search).map(customerMapper::toDto);
        else
            return customerRepository.findAll(pageable).map(customerMapper::toDto);
    }
}
