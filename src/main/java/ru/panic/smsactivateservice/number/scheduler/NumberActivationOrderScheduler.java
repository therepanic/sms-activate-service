package ru.panic.smsactivateservice.number.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.panic.smsactivateservice.number.component.NumberActivationOrderComponent;

@Component
@RequiredArgsConstructor
@Slf4j
public class NumberActivationOrderScheduler {
    private final NumberActivationOrderComponent numberActivationOrderComponent;

    @Scheduled(fixedDelay = 10000)
    public void checkAllOldNumberActivationOrder() {
        numberActivationOrderComponent.getNumberActivationOrderList()
                .removeAll(
                        numberActivationOrderComponent.getNumberActivationOrderList().stream()
                        .filter(n -> System.currentTimeMillis() - n.getTimestamp() >= 30000)
                        .toList()
                );
    }
}
