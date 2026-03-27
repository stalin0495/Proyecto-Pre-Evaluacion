package com.pichincha.customer.infrastructure.output.repository.mapper;

import com.pichincha.customer.infrastructure.output.repository.entity.Person;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    builder = @Builder(disableBuilder = true)
)
public interface PersonMapper {

    com.pichincha.customer.domain.Person convertToDomain(Person entity);

    Person convertToEntity(com.pichincha.customer.domain.Person domain);
}
