package ru.panic.smsactivateservice.number.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.panic.smsactivateservice.number.dto.SmsDto;
import ru.panic.smsactivateservice.number.model.Sms;

@Mapper(componentModel = "spring")
public interface SmsToSmsDtoMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "text", source = "text"),
            @Mapping(target = "receivedAt", source = "receivedAt"),
    })
    SmsDto smsToSmsDto(Sms sms);
}
