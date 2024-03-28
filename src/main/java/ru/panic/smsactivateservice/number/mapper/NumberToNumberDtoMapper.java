package ru.panic.smsactivateservice.number.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.panic.smsactivateservice.number.dto.NumberDto;
import ru.panic.smsactivateservice.number.model.Number;

@Mapper(componentModel = "spring")
public interface NumberToNumberDtoMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "status", source = "status"),
            @Mapping(target = "phoneNumber", source = "phoneNumber"),
            @Mapping(target = "activationCost", source = "activationCost"),
            @Mapping(target = "canGetAnotherSms", source = "canGetAnotherSms"),
            @Mapping(target = "countryCode", source = "countryCode"),
            @Mapping(target = "activationOperator", source = "activationOperator"),
            @Mapping(target = "activationTime", source = "activationTime"),
    })
    NumberDto numberToNumberDto(Number number);
}
