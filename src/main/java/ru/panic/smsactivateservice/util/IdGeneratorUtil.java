package ru.panic.smsactivateservice.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGeneratorUtil {

    private static final SecureRandom random = new SecureRandom();
    private static final AtomicLong lastTime = new AtomicLong();

    public long generateId() {
        long now = System.currentTimeMillis();
        long timeComponent;

        while (true) {
            long last = lastTime.get();
            if (now > last) {
                timeComponent = now;
                if (lastTime.compareAndSet(last, now)) break;
            } else {
                timeComponent = last + 1;
                if (lastTime.compareAndSet(last, timeComponent)) break;
            }
        }

        long randomComponent = random.nextInt(1 << 12);
        return (timeComponent << 12) | randomComponent;
    }

}
