package com.yassir.bank.balance.mapper;

import com.yassir.bank.balance.dto.TransactionRequest;
import com.yassir.bank.balance.dto.TransactionResponse;
import com.yassir.bank.balance.model.Transaction;
import org.mapstruct.Mapper;

@Mapper
public interface TransactionMapper {

    Transaction toTransaction(TransactionRequest request);

    TransactionResponse toDto(Transaction transaction);
}
