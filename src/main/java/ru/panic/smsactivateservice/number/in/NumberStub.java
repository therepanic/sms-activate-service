package ru.panic.smsactivateservice.number.in;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.panic.smsactivateservice.number.model.type.NumberService;
import ru.panic.smsactivateservice.number.service.impl.NumberBaseService;

@Controller
@RequestMapping("/stubs/handler_api.php")
@RequiredArgsConstructor
@Slf4j
public class NumberStub {

    private final NumberBaseService numberBaseService;

    @PostMapping
    public ResponseEntity<String> postHandlerApi (
            @RequestParam(value = "api_key", required = false) String apiKey,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "service", required = false) NumberService service,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "status", required = false) String status
            ) throws JsonProcessingException {

        log.info("Used action {}", action);

        switch (action) {
            case "getNumberV2" -> {
                return numberBaseService.handleActivateV2(apiKey,
                        service,
                        country);
            }

            case "getNumber" -> {
                return numberBaseService.handleActivate(apiKey,
                        service,
                        country);
            }

            case "getHistory" -> {
                return numberBaseService.getAllActivationData(apiKey);
            }

            case "getNumbersStatus" -> {
                return numberBaseService.getAllServiceNumberCount(apiKey);
            }

            case "getBalance" -> {
                return numberBaseService.getBalance(apiKey);
            }

            case "getOperators" -> {
                return numberBaseService.getAllAvailableOperator(apiKey);
            }

            case "а" -> {
                return numberBaseService.getAllServicePrice(apiKey);
            }

            case "getCountries" -> {
                return numberBaseService.getAllAvailableCountry(apiKey);
            }

            case "setStatus" -> {
                return numberBaseService.handleSetActivationStatus(apiKey, id, status);
            }

            case "getStatus" -> {
                return numberBaseService.getActivationStatus(apiKey, id);
            }
        }

        log.warn("Wrong action {}", action);

        return ResponseEntity.badRequest().build();
    }

    @GetMapping
    public ResponseEntity<String> getHandlerApi(
            @RequestParam(value = "api_key", required = false) String apiKey,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "service", required = false) NumberService service,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "status", required = false) String status
    ) throws JsonProcessingException {

        log.info("Used action {}", action);

        switch (action) {
            case "getNumberV2" -> {
                return numberBaseService.handleActivateV2(apiKey,
                        service,
                        country);
            }

            case "getNumber" -> {
                return numberBaseService.handleActivate(apiKey,
                        service,
                        country);
            }

            case "getHistory" -> {
                return numberBaseService.getAllActivationData(apiKey);
            }

            case "getNumbersStatus" -> {
                return numberBaseService.getAllServiceNumberCount(apiKey);
            }

            case "getBalance" -> {
                return numberBaseService.getBalance(apiKey);
            }

            case "getOperators" -> {
                return numberBaseService.getAllAvailableOperator(apiKey);
            }

            case "getPrices" -> {
                return numberBaseService.getAllServicePrice(apiKey);
            }

            case "getCountries" -> {
                return numberBaseService.getAllAvailableCountry(apiKey);
            }

            case "setStatus" -> {
                return numberBaseService.handleSetActivationStatus(apiKey, id, status);
            }

            case "getStatus" -> {
                return numberBaseService.getActivationStatus(apiKey, id);
            }
        }

        log.warn("Wrong action {}", action);

        return ResponseEntity.badRequest().build();
    }
}
