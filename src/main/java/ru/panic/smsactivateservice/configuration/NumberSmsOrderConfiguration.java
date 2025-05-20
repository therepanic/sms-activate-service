package ru.panic.smsactivateservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.panic.smsactivateservice.number.model.NumberSmsOrder;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Configuration
public class NumberSmsOrderConfiguration {

    @Bean(name = "numberSmsOrder")
    public BlockingQueue<NumberSmsOrder> numberSmsOrder() {
        return new LinkedBlockingQueue<>();
    }

}
