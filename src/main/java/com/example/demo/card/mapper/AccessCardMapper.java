package com.example.demo.card.mapper;

import com.example.demo.card.api.dto.AccessCardResponseDto;
import com.example.demo.card.domain.AccessCard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccessCardMapper {
    @Mapping(target = "customerFullName", source = "customer.fullName")
    AccessCardResponseDto toDto(AccessCard card);
}
