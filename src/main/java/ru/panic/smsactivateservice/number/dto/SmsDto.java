package ru.panic.smsactivateservice.number.dto;

import lombok.Data;

@Data
public class SmsDto {
    private long id;

    private String text;

    private String receivedAt;
}
