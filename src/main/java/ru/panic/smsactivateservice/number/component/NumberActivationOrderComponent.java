package ru.panic.smsactivateservice.number.component;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;
import ru.panic.smsactivateservice.number.model.type.NumberActivationOrderStatus;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Data
public class NumberActivationOrderComponent {

    private final List<NumberActivationOrder> numberActivationOrderList = new CopyOnWriteArrayList<>();

    public synchronized Optional<Object> getAndMarkAsBusy() {
        if (numberActivationOrderList.isEmpty()) {
            return Optional.empty();
        } else {
            NumberActivationOrder numberActivationOrder = null;

            synchronized (numberActivationOrderList) {
                for (NumberActivationOrder nu : numberActivationOrderList) {
                    if (nu.getStatus().equals(NumberActivationOrderStatus.FREE) && nu.getPhoneNumber() != null) {
                        numberActivationOrder = nu;
                        break;
                    }
                }
            }

            if (numberActivationOrder == null) {
                return Optional.empty();
            }

            numberActivationOrder.setStatus(NumberActivationOrderStatus.BUSY);

            return Optional.of(numberActivationOrder);
        }
    }

}
