package ru.panic.smsactivateservice.number.component;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class NumberActivationComponent {
    private long telegramNumberCount = 9999;
}
