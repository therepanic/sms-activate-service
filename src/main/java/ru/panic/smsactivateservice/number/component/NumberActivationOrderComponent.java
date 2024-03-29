package ru.panic.smsactivateservice.number.component;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;
import ru.panic.smsactivateservice.number.model.type.NumberActivationOrderStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Data
public class NumberActivationOrderComponent {
    private List<NumberActivationOrder> numberActivationOrderList = Collections.synchronizedList(new ArrayList<>());

    public synchronized Optional<Object> getAndMarkAsBusy() {
        if (numberActivationOrderList.isEmpty()) {
            return Optional.empty();
        } else {
            NumberActivationOrder numberActivationOrder = null;

            for (NumberActivationOrder nu : numberActivationOrderList) {
                if (nu.getStatus().equals(NumberActivationOrderStatus.FREE)) {
                    numberActivationOrder = nu;
                }
            }

            if (numberActivationOrder == null) {
                return Optional.empty();
            }

            numberActivationOrderList.remove(numberActivationOrder);

            numberActivationOrder.setStatus(NumberActivationOrderStatus.BUSY);

            numberActivationOrderList.add(numberActivationOrder);

            return Optional.of(numberActivationOrder);
        }
    }
}
