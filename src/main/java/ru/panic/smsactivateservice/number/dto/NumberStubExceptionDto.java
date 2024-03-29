package ru.panic.smsactivateservice.number.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class NumberStubExceptionDto {
    private String exceptionMessage;
}
