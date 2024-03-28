package ru.panic.smsactivateservice.number.service;

import ru.panic.smsactivateservice.number.dto.NumberActivationDataDto;
import ru.panic.smsactivateservice.number.dto.NumberDto;
import ru.panic.smsactivateservice.number.dto.SmsDto;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;
import ru.panic.smsactivateservice.number.model.type.NumberActivationOrderStatus;
import ru.panic.smsactivateservice.number.model.type.NumberActivationState;
import ru.panic.smsactivateservice.number.payload.CreateNumberSmsRequest;
import ru.panic.smsactivateservice.number.payload.GetAllAvailableCountryResponse;
import ru.panic.smsactivateservice.number.payload.GetAllAvailableOperatorResponse;
import ru.panic.smsactivateservice.number.payload.GetAllServicePriceResponse;

import java.util.List;
import java.util.Map;

public interface NumberService {
    NumberDto getWithLastSeenUpdate(long id);
    NumberDto handleActivate(String apiKey,
                             ru.panic.smsactivateservice.number.model.type.NumberService service,
                             String country);
    NumberActivationState handleSetActivationStatus(String apiKey, long id,
                                                    String status);
    List<NumberActivationDataDto> getAllActivationData(String apiKey);
    List<Map<String, Long>> getAllServiceNumberCount(String apiKey);
    Map<String, Long> getBalance(String apiKey);
    GetAllAvailableOperatorResponse getAllAvailableOperator(String apiKey);
    GetAllAvailableCountryResponse getAllAvailableCountry(String apiKey);
    GetAllServicePriceResponse getAllServicePrice(String apiKey);

    List<NumberActivationOrder> getAllActivationOrder();

    void updateActivationOrderStatus(long id, NumberActivationOrderStatus status);

    void updateActivationOrderPhoneNumber(long id, String phoneNumber);

    SmsDto createSms(CreateNumberSmsRequest createNumberSmsRequest);
}
