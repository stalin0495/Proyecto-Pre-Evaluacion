package com.pichincha.account.infrastructure.output.repository.mapper;

import com.pichincha.account.infrastructure.output.repository.entity.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public interface TransactionMapper {

    Transaction convertToEntity(com.pichincha.account.domain.Transaction transaction);

    com.pichincha.account.domain.Transaction convertToDomain(Transaction transaction);
}
