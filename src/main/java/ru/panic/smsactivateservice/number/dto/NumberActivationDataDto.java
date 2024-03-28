package ru.panic.smsactivateservice.number.dto;

import lombok.Data;

import java.util.List;

@Data
public class NumberActivationDataDto {
    private long id;

    private String date;

    private String phone;

    private List<String> sms;

    private int cost;

    private String status;
}
