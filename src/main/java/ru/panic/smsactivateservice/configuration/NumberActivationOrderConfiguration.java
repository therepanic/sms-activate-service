package ru.panic.smsactivateservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class NumberActivationOrderConfiguration {

    @Bean(name = "numberActivationOrder")
    public BlockingQueue<NumberActivationOrder> numberActivationOrder() {
        return new LinkedBlockingQueue<>();
    }

}
