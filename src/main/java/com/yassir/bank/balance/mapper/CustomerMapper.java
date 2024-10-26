package com.yassir.bank.balance.mapper;

import com.yassir.bank.balance.dto.CustomerRequest;
import com.yassir.bank.balance.dto.CustomerResponse;
import com.yassir.bank.balance.model.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer toCustomer(CustomerRequest request);

    CustomerResponse toDto(Customer customer);
}
