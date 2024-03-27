package ru.panic.smsactivateservice.number.service;

import ru.panic.smsactivateservice.number.dto.NumberDto;

public interface NumberService {
    NumberDto handleActivate(ru.panic.smsactivateservice.number.model.type.NumberService service,
                             String country);
}
