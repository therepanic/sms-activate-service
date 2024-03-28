package ru.panic.smsactivateservice.number.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.panic.smsactivateservice.number.component.NumberActivationComponent;
import ru.panic.smsactivateservice.number.component.NumberActivationOrderComponent;
import ru.panic.smsactivateservice.number.dto.NumberActivationDataDto;
import ru.panic.smsactivateservice.number.dto.NumberDto;
import ru.panic.smsactivateservice.number.dto.SmsDto;
import ru.panic.smsactivateservice.number.mapper.NumberToNumberActivationDataDtoMapperImpl;
import ru.panic.smsactivateservice.number.mapper.NumberToNumberDtoMapperImpl;
import ru.panic.smsactivateservice.number.mapper.SmsToSmsDtoMapperImpl;
import ru.panic.smsactivateservice.number.model.Number;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;
import ru.panic.smsactivateservice.number.model.Sms;
import ru.panic.smsactivateservice.number.model.type.NumberActivationOrderStatus;
import ru.panic.smsactivateservice.number.model.type.NumberActivationState;
import ru.panic.smsactivateservice.number.model.type.NumberOperator;
import ru.panic.smsactivateservice.number.payload.CreateNumberSmsRequest;
import ru.panic.smsactivateservice.number.payload.GetAllAvailableCountryResponse;
import ru.panic.smsactivateservice.number.payload.GetAllAvailableOperatorResponse;
import ru.panic.smsactivateservice.number.payload.GetAllServicePriceResponse;
import ru.panic.smsactivateservice.number.repository.MerchantRepository;
import ru.panic.smsactivateservice.number.repository.NumberRepository;
import ru.panic.smsactivateservice.number.repository.SmsRepository;
import ru.panic.smsactivateservice.number.security.MerchantSecurity;
import ru.panic.smsactivateservice.number.service.NumberService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class NumberBaseService implements NumberService {
    private final NumberRepository numberRepository;
    private final SmsRepository smsRepository;
    private final MerchantRepository merchantRepository;
    private final MerchantSecurity merchantSecurity;
    private final NumberToNumberDtoMapperImpl numberToNumberDtoMapper;
    private final NumberToNumberActivationDataDtoMapperImpl numberToNumberActivationDataDtoMapper;
    private final SmsToSmsDtoMapperImpl smsToSmsDtoMapper;
    private final NumberActivationOrderComponent numberActivationOrderComponent;
    private final NumberActivationComponent numberActivationComponent;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public NumberDto getWithLastSeenUpdate(long id) {
            while (true) {
                boolean isLastSeenUpdated = numberRepository.findIsLastSeenUpdatedById(id);

                if (isLastSeenUpdated) {
                    numberRepository.updateIsLastSeenUpdatedById(false, id);

                    NumberDto responseNumberDto = numberToNumberDtoMapper.numberToNumberDto(numberRepository.findById(id).orElse(null));

                    switch (responseNumberDto.getStatus()) {
                        case "3" -> responseNumberDto.setStatus("2");
                        case "6", "8" -> responseNumberDto.setStatus("3");
                    }

                    return responseNumberDto;
                } else {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        log.warn(e.getMessage());
                    }
                }
            }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public NumberDto handleActivate(String apiKey,
                                    ru.panic.smsactivateservice.number.model.type.NumberService service, String country) {
        // Создаем новый номер в системе

        merchantSecurity.secureByApiKey(apiKey);

        Number newNumber = Number.builder()
                .status("1")
                .activationCost("9")
                .phoneNumber(null)
                .activationOperator(NumberOperator.ROSTELECOM)
                .countryCode("0")
                .canGetAnotherSms("0")
                .activationTime(new Date())
                .isLastSeenUpdated(true)
                .merchant(merchantRepository.findByApiKey(apiKey).orElseThrow())
                .build();

        numberRepository.save(newNumber);

        numberActivationOrderComponent.getNumberActivationOrderList().add(NumberActivationOrder.builder()
                .numberId(newNumber.getId())
                .status(NumberActivationOrderStatus.FREE)
                .build());

        // Ждем, пока физическое устройство начнет обработку заявки на активацию

        while (true) {
            NumberActivationOrder numberIdPhoneNumberForDelete = null;

            // Ищем обработанную заявку устройством, если такая есть

            for (NumberActivationOrder numberActivationOrder : numberActivationOrderComponent.getNumberActivationOrderList()) {
                if (numberActivationOrder.getNumberId() == newNumber.getId()
                        && numberActivationOrder.getPhoneNumber() != null) {

                    numberIdPhoneNumberForDelete = numberActivationOrder;

                    newNumber.setPhoneNumber(numberActivationOrder.getPhoneNumber());

                    numberRepository.save(newNumber);

                    //Найдя нужный ордер на активацию, прекращаем поиск

                    break;
                }
            }

            // Если физическое устройство обработало заявку, завершаем цикл while

            if (numberIdPhoneNumberForDelete != null) {
                numberActivationOrderComponent.getNumberActivationOrderList().remove(numberIdPhoneNumberForDelete);

                return numberToNumberDtoMapper.numberToNumberDto(newNumber);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.warn(e.getMessage());
            }
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public NumberActivationState handleSetActivationStatus(String apiKey, long id, String status) {
        merchantSecurity.secureByApiKey(apiKey);

        Number number = numberRepository.findById(id).orElseThrow();

        number.setStatus(status);
        number.setIsLastSeenUpdated(true);

        numberRepository.save(number);

        switch (status) {
            case "3" -> {
                return NumberActivationState.ACCESS_RETRY_GET;
            }
            case "6", "8" -> {
                return NumberActivationState.ACCESS_READY;
            }
        }

        return null;
    }

    @Override
    public List<NumberActivationDataDto> getAllActivationData(String apiKey) {
        merchantSecurity.secureByApiKey(apiKey);

        List<Number> numberList = numberRepository.findAllByMerchantAndStatusOrMerchantAndStatusOrderByActivationTimeDesc(
                "6",
                "8",
                merchantRepository.findByApiKey(apiKey)
                        .orElseThrow())
                .orElseThrow();

        return numberList.stream()
                .map(numberToNumberActivationDataDtoMapper::numberToNumberActivationDataDto)
                .toList();
    }

    @Override
    public List<Map<String, Long>> getAllServiceNumberCount(String apiKey) {
        merchantSecurity.secureByApiKey(apiKey);

        return List.of(Map.of("tg_0", numberActivationComponent.getTelegramNumberCount()));
    }

    @Override
    public Map<String, Long> getBalance(String apiKey) {
        merchantSecurity.secureByApiKey(apiKey);

        return Map.of("ACCESS_BALANCE", 9999L);
    }

    @Override
    public GetAllAvailableOperatorResponse getAllAvailableOperator(String apiKey) {
        merchantSecurity.secureByApiKey(apiKey);

        return GetAllAvailableOperatorResponse.builder()
                .status("success")
                .countryOperator(GetAllAvailableOperatorResponse.CountryOperator.builder()
                        .russianCountryOperators(List.of("rostelecom"))
                        .build())
                .build();
    }

    @Override
    public GetAllAvailableCountryResponse getAllAvailableCountry(String apiKey) {
        merchantSecurity.secureByApiKey(apiKey);

        return GetAllAvailableCountryResponse.builder()
                .russianCountry(GetAllAvailableCountryResponse.AvailableCountry.builder()
                        .id(0)
                        .rus("Россия")
                        .eng("Russia")
                        .chn("俄罗斯")
                        .visible(1)
                        .retry(1)
                        .rent(1)
                        .multiService(1)
                        .build())
                .build();
    }

    @Override
    public GetAllServicePriceResponse getAllServicePrice(String apiKey) {
        merchantSecurity.secureByApiKey(apiKey);

        return GetAllServicePriceResponse.builder()
                .telegramServicePrice(GetAllServicePriceResponse.TelegramServicePrice.builder()
                        .russianCountryServicePrice(GetAllServicePriceResponse.CountryServicePrice.builder()
                                .price(5)
                                .count(9999)
                                .retailPrice(5)
                                .build())
                        .build())
                .build();
    }

    @Override
    public List<NumberActivationOrder> getAllActivationOrder() {
        return numberActivationOrderComponent.getNumberActivationOrderList();
    }

    @Override
    public void updateActivationOrderStatus(long id, NumberActivationOrderStatus status) {
        for (NumberActivationOrder numberActivationOrder : numberActivationOrderComponent.getNumberActivationOrderList()) {
            if (numberActivationOrder.getNumberId() == id) {
                numberActivationOrder.setStatus(status);

                return;
            }
        }
    }

    @Override
    public void updateActivationOrderPhoneNumber(long id, String phoneNumber) {
        for (NumberActivationOrder numberActivationOrder : numberActivationOrderComponent.getNumberActivationOrderList()) {
            if (numberActivationOrder.getNumberId() == id) {
                numberActivationOrder.setPhoneNumber(phoneNumber);

                return;
            }
        }
    }

    @Override
    public SmsDto createSms(CreateNumberSmsRequest createNumberSmsRequest) {
        Number principalNumber = numberRepository.findById(createNumberSmsRequest.getActivationId())
                .orElse(null);

        Sms newSms = Sms.builder()
                .text(createNumberSmsRequest.getText())
                .number(principalNumber)
                .receivedAt(new Date().toString())
                .build();

        return smsToSmsDtoMapper.smsToSmsDto(smsRepository.save(newSms));
    }
}
