package ru.panic.smsactivateservice.number.component;

import lombok.Data;
import org.springframework.stereotype.Component;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Data
public class NumberActivationOrderComponent {
    private List<NumberActivationOrder> numberActivationOrderList = Collections.synchronizedList(new ArrayList<>());
}
