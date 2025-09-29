package com.pichincha.account.infrastructure.output.repository.mapper;


import com.pichincha.account.domain.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {


    com.pichincha.account.infrastructure.output.repository.entity.Account convertToEntity(
            Account account);

    com.pichincha.account.domain.Account convertToDomain(
            com.pichincha.account.infrastructure.output.repository.entity.Account account);
}
