package ru.panic.smsactivateservice.number.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import ru.panic.smsactivateservice.number.payload.*;
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
    private final ObjectMapper objectMapper;

    @Override
    public GetStatusWithLastSeenUpdateResponse getStatusWithLastSeenUpdate(long id) {
        boolean isLastSeenUpdated = numberRepository.findIsLastSeenUpdatedById(id);

        if (isLastSeenUpdated) {
            Number number = numberRepository.findById(id).orElse(null);

            number.setIsLastSeenUpdated(false);

            numberRepository.save(number);

            NumberDto responseNumberDto = numberToNumberDtoMapper.numberToNumberDto(number);

            switch (responseNumberDto.getStatus()) {
                case "3" -> responseNumberDto.setStatus("2");
                case "6"-> responseNumberDto.setStatus("3");
                case "8" -> responseNumberDto.setStatus("4");
            }

            return GetStatusWithLastSeenUpdateResponse.builder()
                    .status(responseNumberDto.getStatus())
                    .build();
        } else {
            return GetStatusWithLastSeenUpdateResponse.builder()
                    .status("null")
                    .build();
        }
    }

    @Override
    public ResponseEntity<String> handleActivateV2(String apiKey,
                                                 ru.panic.smsactivateservice.number.model.type.NumberService service, String country) throws JsonProcessingException {
        // Создаем новый номер в системе


        Number newNumber = Number.builder()
                .status("1")
                .activationCost("9")
                .phoneNumber(null)
                .activationOperator(NumberOperator.ROSTELECOM)
                .countryCode("0")
                .canGetAnotherSms("0")
                .activationTime(new Date())
                .isLastSeenUpdated(false)
                .merchant(merchantRepository.findByApiKey(apiKey).orElseThrow())
                .build();

        numberRepository.save(newNumber);

        NumberActivationOrder newNumberActivationOrder = NumberActivationOrder.builder()
                .numberId(newNumber.getId())
                .status(NumberActivationOrderStatus.FREE)
                .timestamp(System.currentTimeMillis())
                .build();

        numberActivationOrderComponent.getNumberActivationOrderList().add(newNumberActivationOrder);

        // Ждем, пока физическое устройство начнет обработку заявки на активацию

        long timestamp = System.currentTimeMillis();

        while (System.currentTimeMillis() - timestamp <= 28000) {
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

                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(objectMapper.writeValueAsString(numberToNumberDtoMapper.numberToNumberDto(newNumber)));
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.warn(e.getMessage());
            }
        }

        numberRepository.delete(newNumber);
        numberActivationOrderComponent.getNumberActivationOrderList().remove(newNumberActivationOrder);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<String> handleActivate(String apiKey, ru.panic.smsactivateservice.number.model.type.NumberService service, String country) throws JsonProcessingException {

        // Создаем новый номер в системе


        Number newNumber = Number.builder()
                .status("1")
                .activationCost("9")
                .phoneNumber(null)
                .activationOperator(NumberOperator.ROSTELECOM)
                .countryCode("0")
                .canGetAnotherSms("0")
                .activationTime(new Date())
                .isLastSeenUpdated(false)
                .merchant(merchantRepository.findByApiKey(apiKey).orElseThrow())
                .build();

        numberRepository.save(newNumber);

        NumberActivationOrder newNumberActivationOrder = NumberActivationOrder.builder()
                .numberId(newNumber.getId())
                .status(NumberActivationOrderStatus.FREE)
                .timestamp(System.currentTimeMillis())
                .build();

        numberActivationOrderComponent.getNumberActivationOrderList().add(newNumberActivationOrder);

        // Ждем, пока физическое устройство начнет обработку заявки на активацию

        long timestamp = System.currentTimeMillis();

        while (System.currentTimeMillis() - timestamp <= 28000) {
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

                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body("ACCESS_NUMBER: " + newNumber.getId() + ": " + newNumber.getPhoneNumber());
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                log.warn(e.getMessage());
            }
        }

        numberRepository.delete(newNumber);
        numberActivationOrderComponent.getNumberActivationOrderList().remove(newNumberActivationOrder);

        return ResponseEntity.noContent().build();
    }

    //@Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public ResponseEntity<String> handleSetActivationStatus(String apiKey, long id, String status) {

        Number number = numberRepository.findById(id).orElseThrow();

        number.setStatus(status);
        number.setIsLastSeenUpdated(true);

        numberRepository.save(number);

        switch (status) {
            case "3" -> {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(String.valueOf(NumberActivationState.ACCESS_RETRY_GET));
            }
            case "6", "8" -> {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(String.valueOf(NumberActivationState.ACCESS_READY));
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<String> getActivationStatus(String apiKey, long id) {

        Number number = numberRepository.findById(id).orElseThrow();

        if (number.getStatus().equals("1")) {
            number.setIsLastSeenUpdated(true);

            numberRepository.save(number);
        }

        switch (number.getStatus()) {
            case "1" -> {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(String.valueOf(NumberActivationState.STATUS_WAIT_CODE));
            }

            case "3" -> {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(String.valueOf(NumberActivationState.STATUS_WAIT_RESEND));
            }

            case "6" -> {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(NumberActivationState.STATUS_OK + ":" + number.getSmsList().get(number.getSmsList().size() - 1)
                                .getText());
            }

            case "8" -> {
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_HTML)
                        .body(String.valueOf(NumberActivationState.STATUS_CANCEL));
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @Override
    public ResponseEntity<String> getAllActivationData(String apiKey) throws JsonProcessingException {

        List<Number> numberList = numberRepository.findAllByMerchantAndStatusOrMerchantAndStatusOrderByActivationTimeDesc(
                "6",
                "8",
                merchantRepository.findByApiKey(apiKey)
                        .orElseThrow())
                .orElseThrow();

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(objectMapper.writeValueAsString(numberList.stream()
                        .map(numberToNumberActivationDataDtoMapper::numberToNumberActivationDataDto)
                        .toList()));
    }

    @Override
    public ResponseEntity<String> getAllServiceNumberCount(String apiKey) throws JsonProcessingException {

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(objectMapper.writeValueAsString(List.of(Map.of("tg_0", numberActivationComponent.getTelegramNumberCount()))));
    }

    @Override
    public ResponseEntity<String> getBalance(String apiKey) {

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body("ACCESS_BALANCE: 9999");
    }

    @Override
    public ResponseEntity<String> getAllAvailableOperator(String apiKey) throws JsonProcessingException {

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(objectMapper.writeValueAsString(GetAllAvailableOperatorResponse.builder()
                        .status("success")
                        .countryOperator(GetAllAvailableOperatorResponse.CountryOperator.builder()
                                .russianCountryOperators(List.of("rostelecom"))
                                .build())
                        .build()));
    }

    @Override
    public ResponseEntity<String> getAllAvailableCountry(String apiKey) throws JsonProcessingException {

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(objectMapper.writeValueAsString(GetAllAvailableCountryResponse.builder()
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
                        .build()));
    }

    @Override
    public ResponseEntity<String> getAllServicePrice(String apiKey) throws JsonProcessingException {

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(objectMapper.writeValueAsString(GetAllServicePriceResponse.builder()
                                .russianCountryServicePrice(GetAllServicePriceResponse.CountryServicePrice.builder()
                                        .telegramServicePrice(GetAllServicePriceResponse.ServicePrice.builder()
                                                .cost(5)
                                                .count(9999)
                                                .build())
                                        .build())
                        .build()));
    }

    @Override
    public List<NumberActivationOrder> getAllActivationOrder() {
        return numberActivationOrderComponent.getNumberActivationOrderList();
    }

    @Override
    public Object getRandomActivationOrderExactly() {
        return numberActivationOrderComponent.getAndMarkAsBusy()
                .orElse(GetRandomActivationOrderExactlyResponse.builder().status("null").build());
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
    public UpdateActivationOrderPhoneNumberResponse updateActivationOrderPhoneNumber(long id, String phoneNumber) {
        synchronized (numberActivationOrderComponent.getNumberActivationOrderList()) {
            for (NumberActivationOrder numberActivationOrder : numberActivationOrderComponent.getNumberActivationOrderList()) {
                if (numberActivationOrder.getNumberId() == id && numberActivationOrder.getPhoneNumber() == null) {
                    numberActivationOrder.setPhoneNumber(phoneNumber);

                    return UpdateActivationOrderPhoneNumberResponse.builder()
                            .status("ok")
                            .build();
                }
            }
        }

        return UpdateActivationOrderPhoneNumberResponse.builder()
                .status("null")
                .build();
    }

    @Override
    public SmsDto createSms(CreateNumberSmsRequest createNumberSmsRequest) {
        Number principalNumber = numberRepository.findById(createNumberSmsRequest.getActivationId())
                .orElse(null);

        principalNumber.setStatus("6");
        principalNumber.setIsLastSeenUpdated(false);

        numberRepository.save(principalNumber);

        Sms newSms = Sms.builder()
                .text(createNumberSmsRequest.getText())
                .number(principalNumber)
                .receivedAt(new Date().toString())
                .build();

        return smsToSmsDtoMapper.smsToSmsDto(smsRepository.save(newSms));
    }

}
