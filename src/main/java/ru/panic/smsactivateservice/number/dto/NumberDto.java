package ru.panic.smsactivateservice.number.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import ru.panic.smsactivateservice.number.model.type.NumberOperator;

@Data
public class NumberDto {
    @JsonProperty("activationId")
    private Long id;

    private String phoneNumber;

    private String activationCost;

    private String canGetAnotherSms;

    private String countryCode;

    private NumberOperator activationOperator;

    private String activationTime;
}
