package ru.panic.smsactivateservice.number.in;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.panic.smsactivateservice.number.model.type.NumberService;
import ru.panic.smsactivateservice.number.service.impl.NumberBaseService;

@RestController
@RequestMapping("/stubs/handler_api.php")
@RequiredArgsConstructor
public class NumberStub {

    private final NumberBaseService numberBaseService;

    @PostMapping
    public Object activateNumber(
            @RequestParam(value = "api_key", required = false) String apiKey,
            @RequestParam(value = "action", required = false) String action,
            @RequestParam(value = "service", required = false) NumberService service,
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "status", required = false) String status
            ) {

        switch (action) {
            case "getNumberV2" -> {
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

            }
        }

        return null;
    }
}
