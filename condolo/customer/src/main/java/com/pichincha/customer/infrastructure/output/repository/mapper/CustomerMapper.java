package com.pichincha.customer.infrastructure.output.repository.mapper;

import com.pichincha.customer.domain.Customer;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    builder = @Builder(disableBuilder = true)
)
public interface CustomerMapper {

    Customer convertToDomain(com.pichincha.customer.infrastructure.output.repository.entity.Customer entity);

    com.pichincha.customer.infrastructure.output.repository.entity.Customer convertToEntity(Customer domain);

}
