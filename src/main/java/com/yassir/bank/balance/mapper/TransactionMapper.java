package com.yassir.bank.balance.mapper;

import com.yassir.bank.balance.dto.TransactionRequest;
import com.yassir.bank.balance.dto.TransactionResponse;
import com.yassir.bank.balance.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    TransactionResponse toDto(Transaction transaction);
}
