package ru.panic.smsactivateservice.number.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetRandomActivationOrderExactlyResponse {
    private String status;
}
