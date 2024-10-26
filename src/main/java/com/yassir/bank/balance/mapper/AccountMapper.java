package com.yassir.bank.balance.mapper;

import com.yassir.bank.balance.dto.AccountRequest;
import com.yassir.bank.balance.dto.AccountResponse;
import com.yassir.bank.balance.model.Account;
import com.yassir.bank.balance.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "customer",target = "customerId", qualifiedByName = "mapCustomerToId")
    AccountResponse toDto(Account account);

    @Named("mapCustomerToId")
    public default Long mapCustomerToId(Customer customer) {
        return customer != null ? customer.getId() : null;
    }
}
