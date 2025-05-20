package ru.panic.smsactivateservice.number.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class NumberSmsOrder {

    private long numberId;

    private String phoneNumber;

}
