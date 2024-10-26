package com.yassir.bank.balance.service;

import com.yassir.bank.balance.dto.CustomerRequest;
import com.yassir.bank.balance.dto.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomerService {

    CustomerResponse save(CustomerRequest request);

    Page<CustomerResponse> findAll(Pageable pageable, String search);
}
