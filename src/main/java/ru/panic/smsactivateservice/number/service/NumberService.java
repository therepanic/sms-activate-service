package ru.panic.smsactivateservice.number.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import ru.panic.smsactivateservice.number.dto.NumberActivationDataDto;
import ru.panic.smsactivateservice.number.dto.SmsDto;
import ru.panic.smsactivateservice.number.model.NumberActivationOrder;
import ru.panic.smsactivateservice.number.model.type.NumberActivationOrderStatus;
import ru.panic.smsactivateservice.number.model.type.NumberActivationState;
import ru.panic.smsactivateservice.number.payload.*;

import java.util.List;
import java.util.Map;

public interface NumberService {
    GetStatusWithLastSeenUpdateResponse getStatusWithLastSeenUpdate(long id);
    ResponseEntity<String> handleActivateV2(String apiKey,
                                          ru.panic.smsactivateservice.number.model.type.NumberService service,
                                          String country) throws JsonProcessingException;
    ResponseEntity<String> handleActivate(String apiKey,
                                            ru.panic.smsactivateservice.number.model.type.NumberService service,
                                            String country) throws JsonProcessingException;
    ResponseEntity<String> handleSetActivationStatus(String apiKey, long id,
                                                    String status);
    ResponseEntity<String> getActivationStatus(String apiKey, long id);
    ResponseEntity<String> getAllActivationData(String apiKey) throws JsonProcessingException;
    ResponseEntity<String> getAllServiceNumberCount(String apiKey) throws JsonProcessingException;
    ResponseEntity<String> getBalance(String apiKey);
    ResponseEntity<String> getAllAvailableOperator(String apiKey) throws JsonProcessingException;
    ResponseEntity<String> getAllAvailableCountry(String apiKey) throws JsonProcessingException;
    ResponseEntity<String> getAllServicePrice(String apiKey) throws JsonProcessingException;

    List<NumberActivationOrder> getAllActivationOrder();
    Object getRandomActivationOrderExactly() throws JsonProcessingException;

    void updateActivationOrderStatus(long id, NumberActivationOrderStatus status);

    void updateActivationOrderPhoneNumber(long id, String phoneNumber);

    SmsDto createSms(CreateNumberSmsRequest createNumberSmsRequest);
}
