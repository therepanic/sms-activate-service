package ru.panic.smsactivateservice.number.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.panic.smsactivateservice.number.model.type.NumberActivationOrderStatus;

@Data
@AllArgsConstructor
@Builder
public class NumberActivationOrder {
    private NumberActivationOrderStatus status;
    private long numberId;
    private String phoneNumber;
}
