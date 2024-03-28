package ru.panic.smsactivateservice.number.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.panic.smsactivateservice.number.dto.NumberActivationDataDto;
import ru.panic.smsactivateservice.number.model.Number;
import ru.panic.smsactivateservice.number.model.Sms;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NumberToNumberActivationDataDtoMapper {
    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "date", source = "activationTime"),
            @Mapping(target = "phone", source = "phoneNumber"),
            @Mapping(target = "sms", expression = "java(mapSmsListToStringList(number.getSmsList()))"),
            @Mapping(target = "cost", source = "activationCost"),
            @Mapping(target = "status", source = "status"),
    })
    NumberActivationDataDto numberToNumberActivationDataDto(Number number);

    @Mappings({
            @Mapping(target = "id", source = "id"),
            @Mapping(target = "date", source = "activationTime"),
            @Mapping(target = "phone", source = "phoneNumber"),
            @Mapping(target = "sms", expression = "java(mapSmsListToStringList(number.getSmsList()))"),
            @Mapping(target = "cost", source = "activationCost"),
            @Mapping(target = "status", source = "status"),
    })
    List<NumberActivationDataDto> numberListToNumberActivationDataDtoList(List<Number> numberList);

    default List<String> mapSmsListToStringList(List<Sms> smsList) {
        return smsList.stream()
                .map(Sms::getText)
                .toList();
    }
}
