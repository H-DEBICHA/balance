package com.yassir.bank.balance.mapper;

import com.yassir.bank.balance.dto.AccountRequest;
import com.yassir.bank.balance.dto.AccountResponse;
import com.yassir.bank.balance.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toAccount(AccountRequest request);

    AccountResponse toDto(Account account);
}
