package ru.panic.smsactivateservice.number.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateNumberSmsRequest {
    private long activationId;
    private String text;
}
