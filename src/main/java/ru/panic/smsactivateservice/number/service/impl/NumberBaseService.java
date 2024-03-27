package ru.panic.smsactivateservice.number.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.panic.smsactivateservice.number.component.NumberActivationOrderComponent;
import ru.panic.smsactivateservice.number.dto.NumberDto;
import ru.panic.smsactivateservice.number.mapper.NumberToNumberDtoMapperImpl;
import ru.panic.smsactivateservice.number.model.Number;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;
import ru.panic.smsactivateservice.number.model.type.NumberActivationOrderStatus;
import ru.panic.smsactivateservice.number.model.type.NumberOperator;
import ru.panic.smsactivateservice.number.repository.NumberRepository;
import ru.panic.smsactivateservice.number.service.NumberService;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class NumberBaseService implements NumberService {
    private final NumberRepository numberRepository;
    private final NumberToNumberDtoMapperImpl numberToNumberDtoMapper;
    private final NumberActivationOrderComponent numberActivationOrderComponent;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public NumberDto handleActivate(ru.panic.smsactivateservice.number.model.type.NumberService service, String country) {
        // Создаем новый номер в системе

        Number newNumber = Number.builder()
                .activationCost("9")
                .phoneNumber(null)
                .activationOperator(NumberOperator.ROSTELECOM)
                .countryCode("0")
                .canGetAnotherSms("1")
                .activationTime(new Date().toString())
                .build();

        numberRepository.save(newNumber);

        numberActivationOrderComponent.getNumberActivationOrderList().add(NumberActivationOrder.builder().build());

        // Ждем, пока физическое устройство начнет обработку заявки на активацию

        while (true) {
            NumberActivationOrder numberIdPhoneNumberForDelete = null;

            // Ищем обработанную заявку устройством, если такая есть

            for (NumberActivationOrder numberActivationOrder : numberActivationOrderComponent.getNumberActivationOrderList()) {
                if (numberActivationOrder.getStatus().equals(NumberActivationOrderStatus.BUSY)
                        && numberActivationOrder.getNumberId() == newNumber.getId()
                        && numberActivationOrder.getPhoneNumber() != null) {
                    numberIdPhoneNumberForDelete = numberActivationOrder;

                    newNumber.setPhoneNumber(numberActivationOrder.getPhoneNumber());

                    numberRepository.save(newNumber);
                }
            }

            // Если физическое устройство обработало заявку, завершаем цикл while

            if (numberIdPhoneNumberForDelete != null) {
                numberActivationOrderComponent.getNumberActivationOrderList().remove(numberIdPhoneNumberForDelete);

                break;
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.warn(e.getMessage());
            }
        }


        return null;
    }
}
