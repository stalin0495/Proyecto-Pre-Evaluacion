package com.pichincha.customer.infrastructure.output.repository.mapper;

import com.pichincha.customer.domain.Customer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    Customer convertToDomain(com.pichincha.customer.infrastructure.output.repository.entity.Customer entity);

    com.pichincha.customer.infrastructure.output.repository.entity.Customer convertToEntity(Customer domain);

}
